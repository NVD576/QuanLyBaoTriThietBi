import React, { useEffect, useState } from "react";
import {
 collection,
 addDoc,query,
 orderBy, onSnapshot,
 serverTimestamp,
} from "firebase/firestore";
import { db } from "../configs/Firebase";
import { useContext } from "react";
import { MyUserContext } from "../configs/MyContexts";
import styles from "./Forum.module.css"; // Import CSS Module

function Forum() {
 const [posts, setPosts] = useState([]);
 const [isCreatingPost, setIsCreatingPost] = useState(false); // State để kiểm soát hiển thị form tạo bài viết
 const [newTitle, setNewTitle] = useState("");
 const [newContent, setNewContent] = useState("");
 const [selectedPost, setSelectedPost] = useState(null);
 const [comments, setComments] = useState([]);
 const [newComment, setNewComment] = useState("");
 const user = useContext(MyUserContext);  console.log("User context:", user);

 useEffect(() => {
  const q = query(collection(db, "posts"), orderBy("createdAt", "desc"));
 const unsubscribe = onSnapshot(q, (querySnapshot) => {
 const postsArr = [];
 querySnapshot.forEach((doc) => {
 postsArr.push({ id: doc.id, ...doc.data() });
 });
setPosts(postsArr);
});
 return () => unsubscribe();
}, []);

 useEffect(() => {
    if (!selectedPost) {
      setComments([]);
      return;
    }

    const commentsRef = collection(db, "posts", selectedPost.id, "comments");
    const q = query(commentsRef, orderBy("createdAt", "asc"));

    const unsubscribe = onSnapshot(q, (querySnapshot) => {
      const commentsArr = [];
      querySnapshot.forEach((doc) => {
        commentsArr.push({ id: doc.id, ...doc.data() });
      });
      setComments(commentsArr);
    });
    return () => unsubscribe();
  }, [selectedPost]);

  const openCreatePostForm = () => {
    setIsCreatingPost(true);
    setNewTitle("");
    setNewContent("");
  };

  const closeCreatePostForm = () => {
    setIsCreatingPost(false);
  };

  const addPost = async () => {
    if (!newTitle.trim() || !newContent.trim()) {
      alert("Vui lòng nhập tiêu đề và nội dung!");
      return;
    }

    try {
      await addDoc(collection(db, "posts"), {
        title: newTitle,
        content: newContent,
        createdAt: serverTimestamp(),
        name: user?.name || user?.email || "Ẩn danh",
      });
      setIsCreatingPost(false);
      setNewTitle("");
      setNewContent("");
    } catch (error) {
      alert("Lỗi khi thêm bài viết: " + error.message);
    }
  };

  const addComment = async () => {
    if (!newComment.trim()) {
      alert("Vui lòng nhập nội dung bình luận!");
      return;
    }

    try {
      await addDoc(collection(db, "posts", selectedPost.id, "comments"), {
        content: newComment,
        createdAt: serverTimestamp(),
        name: user?.name || user?.email || "Ẩn danh",
      });
      setNewComment("");
    } catch (error) {
      alert("Lỗi khi thêm bình luận: " + error.message);
    }
  };

// Ở phần render main
return (
  <div className={styles.container}>
    <h1 className={styles.heading}>Forum Firebase</h1>

    <button onClick={openCreatePostForm} className={styles.button}>
      Tạo bài viết mới
    </button>

    {/* Modal tạo bài viết */}
    {isCreatingPost && (
      <div className={styles.overlay}>
        <div className={styles.modal}>
          {isCreatingPost && (
  <div className={styles.overlay}>
    <div className={styles.modal}>
      <h2 className={styles.modalTitle}>Tạo bài viết mới</h2>
      <input
        type="text"
        placeholder="Tiêu đề"
        value={newTitle}
        onChange={(e) => setNewTitle(e.target.value)}
        className={styles.modalInput}
      />
      <textarea
        placeholder="Nội dung"
        value={newContent}
        onChange={(e) => setNewContent(e.target.value)}
        rows={4}
        className={styles.modalTextarea}
      />
      <div className={styles.modalButtonContainer}>
        <button onClick={addPost} className={styles.modalSubmitButton}>
          Đăng bài
        </button>
        <button onClick={closeCreatePostForm} className={styles.modalCancelButton}>
          Hủy
        </button>
      </div>
    </div>
  </div>
)}

        </div>
      </div>
    )}

    {/* Layout 2 cột */}
    <div style={{ display: "flex", gap: "24px", marginTop: "20px" }}>
      {/* Danh sách bài viết */}
      <div style={{ flex: 1, maxWidth: "400px" }}>
        <h2 className={styles.postListTitle}>Danh sách bài viết</h2>
        {posts.length === 0 && <p>Chưa có bài viết nào.</p>}
        <ul className={styles.postList}>
          {posts.map((post) => (
            <li
              key={post.id}
              className={`${styles.postItem} ${
                selectedPost?.id === post.id ? styles.selected : ""
              }`}
              onClick={() => setSelectedPost(post)}
            >
              <strong className={styles.postTitle}>{post.title}</strong>
              <p className={styles.postContentPreview}>
                {post.content.substring(0, 100)}...
              </p>
            </li>
          ))}
        </ul>
      </div>

      {/* Chi tiết bài viết + bình luận */}
      <div style={{ flex: 2 }}>
        {selectedPost ? (
          <div className={styles.postDetailContainer}>
            <h3 className={styles.postDetailTitle}>{selectedPost.title}</h3>
            <p className={styles.postDetailContent}>{selectedPost.content}</p>

            <div className={styles.commentsContainer}>
              <h4 className={styles.commentsTitle}>Bình luận</h4>
              {comments.length === 0 && <p>Chưa có bình luận nào.</p>}
              <ul className={styles.commentList}>
                {comments.map((cmt) => (
                  <li key={cmt.id} className={styles.commentItem}>
                    <strong className={styles.commentAuthor}>
                      {cmt.name || "Ẩn danh"}:
                    </strong>{" "}
                    <span className={styles.commentContent}>{cmt.content}</span>
                  </li>
                ))}
              </ul>

              <textarea
                placeholder="Nhập bình luận..."
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                rows={3}
                className={styles.newCommentTextarea}
              />
              <button onClick={addComment} className={styles.button}>
                Gửi bình luận
              </button>
            </div>

            <button
              onClick={() => setSelectedPost(null)}
              className={styles.closeButton}
            >
              Đóng
            </button>
          </div>
        ) : (
          <p>Chọn một bài viết để xem chi tiết.</p>
        )}
      </div>
    </div>
  </div>
);

}

export default Forum;