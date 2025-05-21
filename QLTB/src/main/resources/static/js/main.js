function deleteDevice(endpoint, id) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint + id, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Có lỗi xảy ra!");
        });
    }
}
function deleteBase(endpoint, id) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint + id, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Có lỗi xảy ra!");
        });
    }
}
function deleteCategory(endpoint, id) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint + id, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Có lỗi xảy ra!");
        });
    }
}
function deleteMaintenance(endpoint) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Có lỗi xảy ra!");
        });
    }
}

function deleteIssue(endpoint) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Có lỗi xảy ra!");
        });
    }
}
function deleteRepair(endpoint) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Có lỗi xảy ra!");
        });
    }
}
function deleteAccount(endpoint) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Có lỗi xảy ra!");
        });
    }
}
function showCostForm(button) {
    const id = button.getAttribute("data-id");
    const form = document.getElementById("cost-form-" + id);
    if (form) {
        form.style.display = "block";
        button.style.display = "none"; // Ẩn nút xác nhận
    }
}

function cancelCostForm(button) {
    const id = button.getAttribute("data-id");
    const form = document.getElementById("cost-form-" + id);
    const confirmButton = document.querySelector('button[data-id="' + id + '"]');
    if (form && confirmButton) {
        form.style.display = "none";
        confirmButton.style.display = "inline-block"; // Hiện lại nút xác nhận
    }
}

document.getElementById("accountForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const formData = new FormData(this);

    fetch("/account", {
        method: "POST",
        body: formData
    }).then(res => {
        if (res.ok) {
            alert("Lưu thành công!");
            location.reload(); // hoặc cập nhật giao diện
        } else {
            alert("Có lỗi xảy ra!");
        }
    });
});



