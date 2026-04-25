package tr.edu.ttmtal.mobil.utils;

public class ImageKitHelper {
    public static String ENDPOINT = ""; // Initialized in TTMTALApp from resources

    /**
     * Converts a local path or filename to a full ImageKit URL.
     * If the path is already a full URL, it returns it as is.
     */
    public static String getUrl(String path) {
        if (path == null || path.isEmpty()) return "";
        if (path.startsWith("http")) return path;
        
        String cleanPath = path;
        if (cleanPath.startsWith("/")) {
            cleanPath = cleanPath.substring(1);
        }
        
        return ENDPOINT + "/" + cleanPath;
    }

    /**
     * Returns a URL with transformation parameters for optimization.
     */
    public static String getOptimizedUrl(String path, int width, int height) {
        String baseUrl = getUrl(path);
        if (baseUrl.isEmpty()) return "";
        
        String separator = baseUrl.contains("?") ? "&" : "?";
        return baseUrl + separator + "tr=w-" + width + ",h-" + height + ",fo-auto";
    }
}
