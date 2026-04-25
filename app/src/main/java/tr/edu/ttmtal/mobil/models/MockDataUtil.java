package tr.edu.ttmtal.mobil.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockDataUtil {

    public static List<NewsModel> getSliderNews() {
        List<NewsModel> list = new ArrayList<>();
        list.add(new NewsModel("1", "Tübitak 4006 Bilim Fuarı Açılışı", "Okulumuzda bilim fuarı düzenlendi.", "slider/slider_1.jpg", "3 gün önce", ""));
        list.add(new NewsModel("2", "Yeni Bilgisayar Laboratuvarımız", "Bilişim bölümü için yeni laboratuvar açıldı.", "slider/slider_2.jpg", "1 hafta önce", ""));
        list.add(new NewsModel("3", "Voleybol Takımımız Şampiyon!", "Voleybol takımımız il birincisi oldu.", "slider/slider_3.jpg", "2 hafta önce", ""));
        return list;
    }

    public static List<NewsModel> getPopularNews() {
        List<NewsModel> list = new ArrayList<>();
        list.add(new NewsModel("4", "Mezuniyet Töreni Hazırlıkları Başladı", "Etkinlikler", "news/news_1.jpg", "1 gün önce", ""));
        list.add(new NewsModel("5", "Erasmus+ Projesi Kapsamında Öğrencilerimiz Avrupa'da", "Haberler", "news/news_2.jpg", "3 gün önce", ""));
        list.add(new NewsModel("6", "1. Dönem Veli Toplantısı Pazar Günü Yapılacaktır", "Duyurular", "news/news_3.jpg", "5 gün önce", ""));
        list.add(new NewsModel("7", "Okul Aile Birliği Kermes Düzenliyor", "Etkinlikler", "news/news_4.jpg", "1 hafta önce", ""));
        list.add(new NewsModel("8", "Sınav Takvimi Açıklandı", "Duyurular", "news/news_5.jpg", "2 hafta önce", ""));
        return list;
    }

    public static List<CategoryModel> getCategories() {
        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel("1", "HABERLER", "categories/cat_news.jpg", ""));
        list.add(new CategoryModel("2", "DUYURULAR", "categories/cat_ann.jpg", ""));
        list.add(new CategoryModel("3", "ETKİNLİKLER", "categories/cat_event.jpg", ""));
        list.add(new CategoryModel("4", "PROJELERİMİZ", "categories/cat_proj.jpg", ""));
        list.add(new CategoryModel("5", "OKULUMUZDAN", "categories/cat_school.jpg", ""));
        list.add(new CategoryModel("6", "E-GÜVENLİK", "categories/cat_esafety.jpg", ""));
        return list;
    }

    public static List<GalleryModel> getGalleryPosts() {
        List<GalleryModel> list = new ArrayList<>();
        list.add(new GalleryModel("1", "Bilişim Teknolojileri Atölye Çalışmaları",
                "Mehmet Yılmaz", "24 Nisan 2026",
                Arrays.asList("gallery/bilisim_1.jpg", "gallery/bilisim_2.jpg",
                        "gallery/bilisim_3.jpg", "gallery/bilisim_4.jpg",
                        "gallery/bilisim_5.jpg", "gallery/bilisim_6.jpg")));
        list.add(new GalleryModel("2", "TÜBİTAK 4006 Bilim Fuarı",
                "Ayşe Demir", "20 Nisan 2026",
                Arrays.asList("gallery/tubitak_1.jpg", "gallery/tubitak_2.jpg",
                        "gallery/tubitak_3.jpg", "gallery/tubitak_4.jpg",
                        "gallery/tubitak_5.jpg")));
        list.add(new GalleryModel("3", "Mezuniyet Töreni 2025-2026",
                "Ali Kaya", "15 Nisan 2026",
                Arrays.asList("gallery/grad_1.jpg", "gallery/grad_2.jpg", "gallery/grad_3.jpg")));
        list.add(new GalleryModel("4", "Voleybol Takımı İl Şampiyonluğu",
                "Fatma Çelik", "10 Nisan 2026",
                Arrays.asList("gallery/voley_1.jpg", "gallery/voley_2.jpg",
                        "gallery/voley_3.jpg", "gallery/voley_4.jpg",
                        "gallery/voley_5.jpg", "gallery/voley_6.jpg", "gallery/voley_7.jpg")));
        return list;
    }
}
