function switchEditCommentForm(button) {
    // 클릭한 버튼과 가장 가까운 comment-container 찾기 (버튼 누른 댓글 영역 찾기)
    const commentItemContainer = button.closest('.comment-item-container');

    const quizId = document.querySelector('.quiz-id').value;
    const commentId = commentItemContainer.querySelector('.commentId').value;

    requestEditCommentForm(commentItemContainer, quizId, commentId);
}
function switchEditReCommentForm(button){
    // 클릭한 버튼과 가장 가까운 re-comment-container 찾기 (버튼 누른 대댓글 영역 찾기)
    const reCommentItemContainer = button.closest('.re-comment-item-container');

    const quizId = document.querySelector('.quiz-id').value;
    const commentId=reCommentItemContainer.querySelector('.commentId').value;

    requestEditCommentForm(reCommentItemContainer,quizId,commentId);
}

function requestEditCommentForm(commentItemContainer, quizId, commentId){
    $.ajax({
        url: '/comment/getEditForm?postId='+quizId+"&commentId="+commentId,
        type: "POST",
        processData: false,
        contentType: false,
        success: function (data) {
            console.log(data);
            $(commentItemContainer).find('.comment-item-body').replaceWith(data);
            commentEditFormWordCount(commentItemContainer);
        },
        error: function (error) {
            console.log(error);
        }
    });
}
