package vn.edu.dlu.ctk47.techmate.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import vn.edu.dlu.ctk47.techmate.model.Banner
import vn.edu.dlu.ctk47.techmate.model.Brand
import vn.edu.dlu.ctk47.techmate.model.Category
import vn.edu.dlu.ctk47.techmate.model.Product

object DataSeeder {
    private const val TAG = "DataSeeder"

    @JvmStatic
    fun seedInitialData() {
        Log.i(TAG, ">>> KHỞI CHẠY SEED DỮ LIỆU <<<")
        val db = FirebaseFirestore.getInstance()
        db.enableNetwork().addOnCompleteListener { task: Task<Void> ->
            Log.d(TAG, "Trạng thái mạng Firestore: ${if (task.isSuccessful) "ONLINE" else "OFFLINE"}")
            //executeSeeding()
            updateAllExistingProducts() // Tự động cập nhật field cho các sản phẩm cũ (p10, p11...)
        }
    }

    private fun executeSeeding() {
        seedCategories()
        seedBrands()
        seedProducts()
        seedBanners()
        Log.i(TAG, ">>> ĐÃ GỬI TẤT CẢ YÊU CẦU. Hãy kiểm tra tab Data trên Firebase! <<<")
    }

    /**
     * Hàm cập nhật nhanh: Tự động thêm variants và ram cho TOÀN BỘ sản phẩm hiện có trên console
     */
    fun updateAllExistingProducts() {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("products")

        collection.get().addOnSuccessListener { snapshot ->
            val batch = db.batch()
            var count = 0

            for (doc in snapshot.documents) {
                val updates = mutableMapOf<String, Any>()

                // Thêm variants mặc định nếu chưa có
                if (!doc.contains("variants")) {
                    updates["variants"] = listOf("128GB", "256GB", "512GB")
                }

                // Thêm ram mặc định nếu chưa có
                if (!doc.contains("ram")) {
                    updates["ram"] = "8GB"
                }

                if (updates.isNotEmpty()) {
                    batch.update(doc.reference, updates)
                    count++
                }
            }

            if (count > 0) {
                batch.commit().addOnSuccessListener {
                    Log.i(TAG, ">>> THÀNH CÔNG: Đã cập nhật nhanh $count sản phẩm cũ (p10, p11...) <<<")
                }.addOnFailureListener { e ->
                    Log.e(TAG, "Lỗi cập nhật hàng loạt: ${e.message}")
                }
            } else {
                Log.i(TAG, "Tất cả sản phẩm đã có đủ các field mới.")
            }
        }
    }

    private fun seedCategories() {
        val categories = listOf(
            Category("cat1", "Laptop", "https://cdn-icons-png.flaticon.com/512/428/428001.png", 1),
            Category("cat2", "Smartphone", "https://cdn-icons-png.flaticon.com/512/644/644458.png", 2),
            Category("cat3", "Accessories", "https://cdn-icons-png.flaticon.com/512/3659/3659899.png", 3),
            Category("cat4", "Tablet", "https://cdn-icons-png.flaticon.com/512/688/688531.png", 4)
        )
        val collection: CollectionReference = FirestoreHelper.getCategoriesCollection()
        categories.forEach { category -> 
            category.id?.let { id -> 
                collection.document(id).set(category) 
            } 
        }
    }

    private fun seedBrands() {
        val brands = listOf(
            Brand("brand1", "Apple", "Think Different", "https://upload.wikimedia.org/wikipedia/commons/f/fa/Apple_logo_black.svg"),
            Brand("brand2", "Samsung", "Better Together", "https://upload.wikimedia.org/wikipedia/commons/2/24/Samsung_Logo.svg"),
            Brand("brand3", "Asus", "In Search of Incredible", "https://upload.wikimedia.org/wikipedia/commons/d/de/Asus_Logo.svg"),
            Brand("brand4", "Dell", "The power to do more", "https://upload.wikimedia.org/wikipedia/commons/1/18/Dell_logo_2016.svg")
        )
        val collection: CollectionReference = FirestoreHelper.getBrandsCollection()
        brands.forEach { brand -> 
            brand.id?.let { id -> 
                collection.document(id).set(brand) 
            } 
        }
    }

    private fun seedProducts() {
        val products = mutableListOf<Product>()

        products.add(Product(
            id = "p6", 
            name = "iPhone 15 Pro Max", 
            price = 1199.0, 
            categoryId = "cat2", 
            brandId = "brand1",
            colors = listOf("Natural Titanium", "Blue Titanium", "White", "Black"),
            variants = listOf("256GB", "512GB", "1TB"),
            ram = "8GB",
            specs = mapOf("Screen" to "6.7 inch ProMotion", "CPU" to "A17 Pro"),
            images = listOf("https://m.media-amazon.com/images/I/81+GIkwqLIL._AC_SL1500_.jpg"),
            description = "The first iPhone to feature an aviation‑grade titanium design.",
            rating = 4.9, 
            stock = 20
        ))

        products.add(Product(
            id = "p7", 
            name = "Galaxy S24 Ultra", 
            price = 1299.0, 
            categoryId = "cat2", 
            brandId = "brand2",
            colors = listOf("Titanium Gray", "Titanium Black", "Titanium Violet", "Titanium Yellow"),
            variants = listOf("256GB", "512GB", "1TB"),
            ram = "12GB",
            specs = mapOf("Screen" to "6.8 inch AMOLED", "CPU" to "Snapdragon 8 Gen 3"),
            images = listOf("https://images.samsung.com/is/image/samsung/p6pim/vn/2401/gallery/vn-galaxy-s24-s928-sm-s928bztqvna-thumb-539294529"),
            description = "Galaxy AI is here. Epic, just like that.",
            rating = 4.8, 
            stock = 25
        ))

        products.add(Product(
            id = "p1", 
            name = "MacBook Pro M3", 
            price = 1999.0, 
            categoryId = "cat1", 
            brandId = "brand1",
            colors = listOf("Space Black", "Silver"),
            variants = listOf("512GB", "1TB", "2TB"),
            ram = "18GB",
            specs = mapOf("Screen" to "14.2 inch", "CPU" to "M3 Pro"),
            images = listOf("https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/mbp14-spacegray-select-202310?wid=904&hei=840&fmt=jpeg&qlt=90&.v=1697308945216"),
            description = "The most advanced chips ever built for a personal computer.",
            rating = 4.9, 
            stock = 10
        ))

        val collection: CollectionReference = FirestoreHelper.getProductsCollection()
        products.forEach { product -> 
            product.id?.let { id -> 
                collection.document(id).set(product) 
            } 
        }
    }

    private fun seedBanners() {
        val banners = listOf(
            Banner("banner1", "https://m.media-amazon.com/images/I/71TPda7cwUL._AC_SL1500_.jpg", "p1", "Big Sale MacBook Pro", "Up to 20% Off")
        )
        val db = FirebaseFirestore.getInstance()
        val collection: CollectionReference = db.collection("banners")
        banners.forEach { banner -> 
            banner.id?.let { id -> 
                collection.document(id).set(banner) 
            } 
        }
    }
}
