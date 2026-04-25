// Import config (This file is ignored by git for security)
import firebaseConfigLocal from './firebase-config.js';

const firebaseConfig = firebaseConfigLocal || {
    apiKey: "YOUR_API_KEY",
    authDomain: "YOUR_PROJECT_ID.firebaseapp.com",
    databaseURL: "https://YOUR_PROJECT_ID-default-rtdb.firebaseio.com",
    projectId: "YOUR_PROJECT_ID",
    storageBucket: "YOUR_PROJECT_ID.firebasestorage.app",
    messagingSenderId: "YOUR_SENDER_ID",
    appId: "YOUR_APP_ID"
};

// Initialize Firebase (Compat Version)
firebase.initializeApp(firebaseConfig);
const auth = firebase.auth();
const db = firebase.database();

let currentUser = null;
let isAdmin = false;
let isApproved = false;

// UI Helpers
const showScreen = (id) => {
    document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
    const target = document.getElementById(id);
    if(target) target.classList.add('active');
};

const showModal = (title, formHtml, onSave) => {
    const modal = document.getElementById('modal');
    document.getElementById('modal-title').innerText = title;
    document.getElementById('modal-form-container').innerHTML = formHtml;
    modal.classList.add('active');
    
    document.getElementById('btn-modal-save').onclick = async () => {
        await onSave();
        modal.classList.remove('active');
    };
};

document.getElementById('btn-modal-cancel').onclick = () => document.getElementById('modal').classList.remove('active');

// --- Auth Handling ---
auth.onAuthStateChanged(async (user) => {
    if (user) {
        currentUser = user;
        
        const adminSnap = await db.ref(`admins/${user.uid}`).get();
        isAdmin = adminSnap.exists();
        const teacherSnap = await db.ref(`teachers/${user.uid}`).get();
        isApproved = teacherSnap.exists() && teacherSnap.val().approved;

        if (isAdmin || isApproved) {
            showScreen('main-screen');
            document.getElementById('user-display-name').innerText = isAdmin ? "Yönetici" : user.email.split('@')[0];
            document.getElementById('user-role').innerText = isAdmin ? 'Sistem Yöneticisi' : 'Öğretmen';
            if (!isAdmin) document.querySelectorAll('.admin-only').forEach(el => el.style.display = 'none');
            initDashboard();
        } else {
            alert('Hesabınız onay bekliyor veya yetkiniz yok.');
            auth.signOut();
        }
    } else {
        showScreen('login-screen');
    }
});

// Navigation
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', (e) => {
        const sectionId = item.getAttribute('data-section');
        document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));
        item.classList.add('active');
        document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
        document.getElementById(`section-${sectionId}`).classList.add('active');
        loadData(sectionId);
    });
});

// --- Login Logic ---
document.getElementById('btn-login').onclick = async () => {
    let email = document.getElementById('login-email').value;
    const pass = document.getElementById('login-password').value;

    try {
        await auth.signInWithEmailAndPassword(email, pass);
    } catch (err) {
        alert('Giriş hatası: ' + err.message);
    }
};

document.getElementById('btn-logout').onclick = () => auth.signOut();

// --- Content Management ---
async function incrementVersion(node) {
    const vRef = db.ref(`app_version/${node}`);
    const snap = await vRef.get();
    const cur = snap.exists() ? snap.val() : 0;
    await vRef.set(cur + 1);
}

const loadData = (section) => {
    if (section === 'approvals' && isAdmin) loadApprovals();
    if (section === 'news') loadNews();
    if (section === 'gallery') loadGallery();
    if (section === 'calendar') loadCalendar();
    if (section === 'notes') loadNotes();
    if (section === 'categories') loadCategories();
    if (section === 'notifications') initNotifications();
};

// --- ImageKit Helper ---
async function uploadToImageKit(file) {
    const privateKey = "private_QVdj2WshIfJZ25LVsVGw+P8w2Ak=";
    const formData = new FormData();
    formData.append("file", file);
    formData.append("fileName", file.name);
    formData.append("useUniqueFileName", "true");

    const response = await fetch("https://upload.imagekit.io/api/v1/files/upload", {
        method: "POST",
        headers: {
            "Authorization": "Basic " + btoa(privateKey + ":")
        },
        body: formData
    });

    if (!response.ok) throw new Error("Yükleme başarısız oldu.");
    const data = await response.json();
    return data.url;
}

const getFileInputHtml = (id, multiple = false) => `
    <div style="margin-top:10px; padding:15px; background:rgba(255,255,255,0.05); border-radius:12px; border:1px dashed var(--border-color);">
        <label style="display:block; margin-bottom:8px; font-size:12px; color:var(--text-dim);">Veya Dosya Seçin (ImageKit'e yüklenir)</label>
        <input type="file" id="${id}-file" ${multiple ? 'multiple' : ''} style="margin-bottom:0;" onchange="handleFileSelect(this, '${id}', ${multiple})">
        <small id="${id}-status" style="display:block; margin-top:5px; font-size:11px;"></small>
    </div>
`;

window.handleFileSelect = async (input, targetId, isMultiple) => {
    const status = document.getElementById(`${targetId}-status`);
    if (!input.files.length) return;
    
    status.innerText = input.files.length > 1 ? `${input.files.length} dosya yükleniyor...` : "Yükleniyor...";
    status.style.color = "var(--accent-color)";
    
    try {
        const urls = [];
        for (let file of input.files) {
            const url = await uploadToImageKit(file);
            urls.push(url);
        }
        
        const targetInput = document.getElementById(targetId);
        if (isMultiple) {
            const existing = targetInput.value ? targetInput.value.split(',').map(s => s.trim()).filter(s => s) : [];
            targetInput.value = [...existing, ...urls].join(', ');
        } else {
            targetInput.value = urls[0];
        }
        
        status.innerText = "Yükleme Başarılı!";
        status.style.color = "var(--success)";
    } catch (e) {
        status.innerText = "Hata: " + e.message;
        status.style.color = "var(--danger)";
    }
};

function initDashboard() {
    db.ref('news').on('value', s => document.getElementById('stat-news').innerText = s.exists() ? Object.keys(s.val()).length : 0);
    db.ref('calendar').on('value', s => document.getElementById('stat-events').innerText = s.exists() ? Object.keys(s.val()).length : 0);
    db.ref('teachers').on('value', s => document.getElementById('stat-teachers').innerText = s.exists() ? Object.keys(s.val()).length : 0);
}

// --- Specific Loaders ---
async function loadApprovals() {
    const snap = await db.ref('teachers').get();
    const tbody = document.querySelector('#table-approvals tbody');
    tbody.innerHTML = '';
    if (snap.exists()) {
        Object.entries(snap.val()).forEach(([uid, data]) => {
            if (!data.approved) {
                const tr = document.createElement('tr');
                tr.innerHTML = `<td>${data.name}</td><td>${data.email}</td><td>${new Date(data.timestamp).toLocaleDateString()}</td>
                    <td><button class="action-btn approve-btn" onclick="approveTeacher('${uid}')"><i class="fas fa-check"></i> Onayla</button></td>`;
                tbody.appendChild(tr);
            }
        });
    }
}
window.approveTeacher = async (uid) => { await db.ref(`teachers/${uid}`).update({ approved: true }); loadApprovals(); };

async function loadNews() {
    const snap = await db.ref('news').get();
    const grid = document.getElementById('grid-news');
    grid.innerHTML = '';
    if (snap.exists()) {
        Object.entries(snap.val()).forEach(([id, item]) => {
            const card = document.createElement('div');
            card.className = 'content-card';
            card.innerHTML = `
                <div class="card-img" style="background-image: url('${item.imageUrl || ''}')"></div>
                <div class="card-body">
                    <span class="card-tag">${item.description || 'Haber'}</span>
                    <h4>${item.title}</h4>
                    <div class="card-actions">
                        <button class="icon-btn edit" onclick="editItem('news', '${id}')"><i class="fas fa-edit"></i></button>
                        <button class="icon-btn delete" onclick="deleteItem('news', '${id}')"><i class="fas fa-trash"></i></button>
                    </div>
                </div>`;
            grid.appendChild(card);
        });
    }
}

async function loadGallery() {
    const snap = await db.ref('gallery').get();
    const grid = document.getElementById('grid-gallery');
    grid.innerHTML = '';
    if (snap.exists()) {
        Object.entries(snap.val()).forEach(([id, item]) => {
            const card = document.createElement('div');
            card.className = 'content-card';
            const firstImg = item.imageUrls ? item.imageUrls[0] : '';
            card.innerHTML = `
                <div class="card-img" style="background-image: url('${firstImg}')"></div>
                <div class="card-body">
                    <span class="card-tag">${item.imageUrls ? item.imageUrls.length : 0} Fotoğraf</span>
                    <h4>${item.title}</h4>
                    <div class="card-actions">
                        <button class="icon-btn edit" onclick="editItem('gallery', '${id}')"><i class="fas fa-edit"></i></button>
                        <button class="icon-btn delete" onclick="deleteItem('gallery', '${id}')"><i class="fas fa-trash"></i></button>
                    </div>
                </div>`;
            grid.appendChild(card);
        });
    }
}

async function loadCalendar() {
    const snap = await db.ref('calendar').get();
    const tbody = document.querySelector('#table-calendar tbody');
    tbody.innerHTML = '';
    if (snap.exists()) {
        Object.entries(snap.val()).forEach(([id, item]) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong>${item.title}</strong></td>
                <td>${item.date}</td>
                <td>
                    <button class="icon-btn edit" onclick="editItem('calendar', '${id}')"><i class="fas fa-edit"></i></button>
                    <button class="icon-btn delete" onclick="deleteItem('calendar', '${id}')"><i class="fas fa-trash"></i></button>
                </td>`;
            tbody.appendChild(tr);
        });
    }
}

async function loadCategories() {
    const snap = await db.ref('categories').get();
    const grid = document.getElementById('grid-categories');
    grid.innerHTML = '';
    if (snap.exists()) {
        Object.entries(snap.val()).forEach(([id, item]) => {
            const card = document.createElement('div');
            card.className = 'content-card';
            card.innerHTML = `
                <div class="card-img" style="background-image: url('${item.imageUrl || ''}')"></div>
                <div class="card-body">
                    <span class="card-tag">Kategori</span>
                    <h4>${item.name}</h4>
                    <div class="card-actions">
                        <button class="icon-btn edit" onclick="editItem('categories', '${id}')"><i class="fas fa-edit"></i></button>
                        <button class="icon-btn delete" onclick="deleteItem('categories', '${id}')"><i class="fas fa-trash"></i></button>
                    </div>
                </div>`;
            grid.appendChild(card);
        });
    }
}

async function loadNotes() {
    const snap = await db.ref('notes').get();
    const grid = document.getElementById('grid-notes');
    grid.innerHTML = '';
    if (snap.exists()) {
        Object.entries(snap.val()).forEach(([id, item]) => {
            const card = document.createElement('div');
            card.className = 'content-card';
            card.innerHTML = `
                <div class="card-body">
                    <span class="card-tag">Not</span>
                    <h4>${item.title}</h4>
                    <p style="font-size:12px; color:var(--text-dim);">${item.content ? item.content.substring(0, 100) + '...' : ''}</p>
                    <div class="card-actions">
                        <button class="icon-btn edit" onclick="editItem('notes', '${id}')"><i class="fas fa-edit"></i></button>
                        <button class="icon-btn delete" onclick="deleteItem('notes', '${id}')"><i class="fas fa-trash"></i></button>
                    </div>
                </div>`;
            grid.appendChild(card);
        });
    }
}

window.editItem = async (type, id) => {
    const snap = await db.ref(`${type}/${id}`).get();
    const item = snap.val();
    let html = '';
    if (type === 'news') {
        html = `
            <input id="f-title" placeholder="Başlık" value="${item.title}">
            <input id="f-desc" placeholder="Kategori (Haberler/Duyurular)" value="${item.description}">
            <textarea id="f-content" placeholder="Haber İçeriği/Detayları" style="width:100%; height:150px; background:rgba(0,0,0,0.1); padding:10px; border:1px solid #ddd; margin-bottom:10px;">${item.content || ''}</textarea>
            <input id="f-img" placeholder="Resim Yolu" value="${item.imageUrl}">
            ${getFileInputHtml('f-img')}
        `;
    } else if (type === 'calendar') {
        html = `
            <input id="f-title" placeholder="Etkinlik Adı" value="${item.title}">
            <input id="f-date" placeholder="Tarih" value="${item.date}">
        `;
    } else if (type === 'gallery') {
        html = `
            <input id="f-title" placeholder="Albüm Başlığı" value="${item.title}">
            <input id="f-author" placeholder="Ekleyen" value="${item.authorName}">
            <input id="f-urls" placeholder="Resim Yolları (Virgülle ayırın)" value="${item.imageUrls ? item.imageUrls.join(', ') : ''}">
            ${getFileInputHtml('f-urls', true)}
        `;
    } else if (type === 'categories') {
        html = `
            <input id="f-name" placeholder="Kategori Adı" value="${item.name}">
            <textarea id="f-content" placeholder="Kategori İçeriği/Detayları" style="width:100%; height:150px; background:rgba(0,0,0,0.1); padding:10px; border:1px solid #ddd; margin-bottom:10px;">${item.content || ''}</textarea>
            <input id="f-img" placeholder="Kategori Resim URL" value="${item.imageUrl}">
            ${getFileInputHtml('f-img')}
        `;
    } else if (type === 'notes') {
        html = `
            <input id="f-title" placeholder="Not Başlığı" value="${item.title}">
            <textarea id="f-content" placeholder="Not İçeriği" style="width:100%; height:150px; background:rgba(0,0,0,0.2); color:white; border:1px solid var(--border-color); padding:10px; border-radius:8px;">${item.content || ''}</textarea>
        `;
    }
    
    showModal('Düzenle', html, async () => {
        let updatedData = { ...item };
        if (type === 'news') {
            updatedData = { ...updatedData, title: document.getElementById('f-title').value, description: document.getElementById('f-desc').value, imageUrl: document.getElementById('f-img').value };
        } else if (type === 'calendar') {
            updatedData = { ...updatedData, title: document.getElementById('f-title').value, date: document.getElementById('f-date').value };
        } else if (type === 'gallery') {
            const urls = document.getElementById('f-urls').value.split(',').map(s => s.trim());
            updatedData = { ...updatedData, title: document.getElementById('f-title').value, authorName: document.getElementById('f-author').value, imageUrls: urls };
        } else if (type === 'categories') {
            updatedData = { ...updatedData, name: document.getElementById('f-name').value, imageUrl: document.getElementById('f-img').value };
        } else if (type === 'notes') {
            updatedData = { ...updatedData, title: document.getElementById('f-title').value, content: document.getElementById('f-content').value };
        }
        await db.ref(`${type}/${id}`).set(updatedData);
        await incrementVersion(type);
        loadData(type);
    });
};

document.querySelectorAll('.add-btn').forEach(btn => {
    btn.onclick = () => {
        const type = btn.getAttribute('data-type');
        let html = '';
        if (type === 'news') {
            html = `<input id="f-title" placeholder="Başlık"><input id="f-desc" placeholder="Kategori (Haberler/Duyurular)"><textarea id="f-content" placeholder="Haber İçeriği/Detayları" style="width:100%; height:150px; background:rgba(0,0,0,0.1); padding:10px; border:1px solid #ddd; margin-bottom:10px;"></textarea><input id="f-img" placeholder="Resim Yolu">${getFileInputHtml('f-img')}`;
        } else if (type === 'calendar') {
            html = `<input id="f-title" placeholder="Etkinlik Adı"><input id="f-date" placeholder="Tarih (Örn: 25 Mayıs)">`;
        } else if (type === 'gallery') {
            html = `<input id="f-title" placeholder="Albüm Başlığı"><input id="f-author" placeholder="Ekleyen"><input id="f-urls" placeholder="Resim Yolları (Virgülle ayırın)">${getFileInputHtml('f-urls', true)}`;
        } else if (type === 'categories') {
            html = `<input id="f-name" placeholder="Kategori Adı"><textarea id="f-content" placeholder="Kategori İçeriği/Detayları" style="width:100%; height:150px; background:rgba(0,0,0,0.1); padding:10px; border:1px solid #ddd; margin-bottom:10px;"></textarea><input id="f-img" placeholder="Kategori Resim URL">${getFileInputHtml('f-img')}`;
        } else if (type === 'notes') {
            html = `<input id="f-title" placeholder="Not Başlığı"><textarea id="f-content" placeholder="Not İçeriği" style="width:100%; height:150px; background:rgba(0,0,0,0.2); color:white; border:1px solid var(--border-color); padding:10px; border-radius:8px;"></textarea>`;
        }
        showModal('Yeni Ekle', html, async () => {
            const id = db.ref(type).push().key;
            let data = { id };
            if (type === 'news') {
                data = { ...data, title: document.getElementById('f-title').value, description: document.getElementById('f-desc').value, content: document.getElementById('f-content').value, imageUrl: document.getElementById('f-img').value, date: 'Şimdi' };
            } else if (type === 'calendar') {
                data = { ...data, title: document.getElementById('f-title').value, date: document.getElementById('f-date').value };
            } else if (type === 'gallery') {
                const urls = document.getElementById('f-urls').value.split(',').map(s => s.trim());
                data = { ...data, title: document.getElementById('f-title').value, authorName: document.getElementById('f-author').value, imageUrls: urls, date: 'Bugün' };
            } else if (type === 'categories') {
                data = { ...data, name: document.getElementById('f-name').value, content: document.getElementById('f-content').value, imageUrl: document.getElementById('f-img').value };
            } else if (type === 'notes') {
                data = { ...data, title: document.getElementById('f-title').value, content: document.getElementById('f-content').value, date: 'Yeni' };
            }
            await db.ref(`${type}/${id}`).set(data);
            await incrementVersion(type);
            loadData(type);
        });
    };
});

window.deleteItem = async (type, id) => {
    if (confirm('Silmek istediğinize emin misiniz?')) {
        await db.ref(`${type}/${id}`).remove();
        await incrementVersion(type);
        loadData(type);
    }
};

function initNotifications() {
    const btn = document.getElementById('btn-send-push');
    if (!btn) return;
    
    btn.onclick = async () => {
        const title = document.getElementById('notif-title').value;
        const body = document.getElementById('notif-body').value;
        
        if (!title || !body) {
            alert('Lütfen başlık ve mesaj girin.');
            return;
        }

        if (confirm('Bu bildirim TÜM KULLANICILARA gönderilecek. Emin misiniz?')) {
            const newNotif = {
                title,
                body,
                sentAt: Date.now(),
                sentBy: auth.currentUser ? auth.currentUser.email : 'Admin'
            };
            
            await db.ref('notifications').push(newNotif);
            alert('Bildirim kuyruğa alındı ve gönderiliyor!');
            document.getElementById('notif-title').value = '';
            document.getElementById('notif-body').value = '';
        }
    };
}
