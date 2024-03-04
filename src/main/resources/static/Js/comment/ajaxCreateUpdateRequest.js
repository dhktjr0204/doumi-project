function clickCommentSubmitButton(button){
    const commentForm=button.closest('.comment-form');
    submitCommentForm(commentForm);
}

function clickReCommentSubmitButton(button){
    const commentForm=button.closest('.re-comment-form');
    submitCommentForm(commentForm);
}

function clickCommentEditButton(button){
    const commentForm=button.closest('.comment-form');
    const commentItemContainer= button.closest('.re-comment-item-container') || button.closest('.comment-item-container');
    const commentId= commentItemContainer.querySelector('.comment-id').value;
    editCommentForm(commentForm, commentId);
}

//삭제 버튼
function  clickDeleteButton(button){
    const confirmed= window.confirm("정말 삭제하시겠습니까?");

    if(confirmed) {
        const postId = document.querySelector('.post-id').value;
        const commentItemContainer = button.closest('.re-comment-item-container') || button.closest('.comment-item-container');
        const commentId = commentItemContainer.querySelector('.comment-id').value;
        deleteComment(postId, commentId);
    }else{
        alert("삭제가 취소 되었습니다.");
    }

}

//좋아요 순
function clickOrderByLikeCount(){
    const postId = document.querySelector('.post-id').value;
    const type=document.querySelector('.type').value;
    $.ajax({
        url: "/comment/orderlike?id="+postId+"&type="+type,
        type: "POST",
        processData: false,
        contentType: false,
        success: function (data) {
            $('.comment-main').html(data);
        },
        error: function (error) {
            console.log(error);
        }
    });
}

//최신순
function clickOrderByCreatedAt(){
    const postId = document.querySelector('.post-id').value;
    const type=document.querySelector('.type').value;
    $.ajax({
        url: "/comment/ordertime?id="+postId+"&type="+type,
        type: "POST",
        processData: false,
        contentType: false,
        success: function (data) {
            $('.comment-main').html(data);
        },
        error: function (error) {
            console.log(error);
        }
    });
}


function submitCommentForm(commentForm) {
    const formData = new FormData(commentForm);
    $.ajax({
        url: commentForm.action,
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            $('.comment-container').html(data);
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function requestEditCommentForm(commentItemContainer, comment){
    $.ajax({
        url: '/comment/editForm',
        type: "POST",
        data: JSON.stringify(comment),
        processData: false,
        contentType: 'application/json',
        success: function (data) {
            $(commentItemContainer).children('.comment-item-body').replaceWith(data);
            commentEditFormWordCount(commentItemContainer);
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function editCommentForm(commentForm,commentId){
    const formData = new FormData(commentForm);
    $.ajax({
        url: "/comment/edit?id="+commentId,
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            $('.comment-container').html(data);
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function deleteComment(postId, commentId){
    $.ajax({
        url: "/comment/delete?postId="+postId+"&commentId="+commentId,
        type: "DELETE",
        processData: false,
        contentType: false,
        success: function (data) {
            $('.comment-container').html(data);
        },
        error: function (error) {
            console.log(error);
        }
    });
}
