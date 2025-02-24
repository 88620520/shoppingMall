package com.example.shoppingmall.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageLoader {

    // 存储已加载的图片路径缓存
    private static final Map<String, String> imageCache = new ConcurrentHashMap<>();
    private static ImageLoader instance;

    // 私有构造方法，防止外部创建实例
    private ImageLoader() {}

    // 获取单例实例
    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    // 加载图片并缓存路径
    public String loadImage(String imagePath) {
        // 如果图片路径已缓存，则直接返回缓存的路径
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        }

        // 否则从磁盘加载图片
        // 这里的逻辑可以根据需求做处理，比如判断图片是否存在
        loadImageFromDisk(imagePath);
        imageCache.put(imagePath, imagePath);
        return imagePath;
    }

    // 从磁盘加载图片（假设此方法已实现）
    private void loadImageFromDisk(String imagePath) {
        // 这里可以根据实际需求加载图片
        // 例如检查文件是否存在等
    }

    // 清理缓存
    public void clearCache() {
        imageCache.clear();
    }
}
