package vn.edu.dlu.ctk47.techmate.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import vn.edu.dlu.ctk47.techmate.model.User

object AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val usersCollection = FirestoreHelper.getUsersCollection()

    /**
     * Đăng ký tài khoản mới và lưu thông tin vào Firestore
     */
    fun register(user: User, password: String, onComplete: (Boolean, String?) -> Unit) {
        val email = user.email
        if (email == null) {
            onComplete(false, "Email is required")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    if (uid != null) {
                        user.id = uid
                        // Lưu thông tin user trực tiếp (password không có trong model nên không lo bị lộ)
                        usersCollection.document(uid).set(user)
                            .addOnSuccessListener {
                                onComplete(true, null)
                            }
                            .addOnFailureListener { e ->
                                onComplete(false, e.message)
                            }
                    }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    /**
     * Đăng nhập bằng Email và Password
     */
    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    /**
     * Lấy profile chi tiết từ Firestore
     */
    fun getUserProfile(uid: String, onComplete: (User?) -> Unit) {
        usersCollection.document(uid).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                onComplete(user)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    /**
     * Cập nhật thông tin profile lên Firestore
     */
    fun updateUserProfile(user: User, onComplete: (Boolean, String?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            user.id = uid
            usersCollection.document(uid).set(user)
                .addOnSuccessListener {
                    onComplete(true, null)
                }
                .addOnFailureListener { e ->
                    onComplete(false, e.message)
                }
        } else {
            onComplete(false, "Người dùng chưa đăng nhập")
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser
}
