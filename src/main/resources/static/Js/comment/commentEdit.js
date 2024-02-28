function switchEditCommentForm(button) {
    // 클릭한 버튼과 가장 가까운 comment-container 찾기 (버튼 누른 댓글 영역 찾기)
    const commentItemContainer = button.closest('.comment-item-container');

    const comment = getCommentData(commentItemContainer,"comment");

    // comment/ajaxCreatUpdateRequest.js
    requestEditCommentForm(commentItemContainer, comment);
    editButtonHideControl(commentItemContainer);
}
function switchEditReCommentForm(button){
    // 클릭한 버튼과 가장 가까운 re-comment-container 찾기 (버튼 누른 대댓글 영역 찾기)
    const reCommentItemContainer = button.closest('.re-comment-item-container');

    const comment = getCommentData(reCommentItemContainer,"reComment");

    // comment/ajaxCreatUpdateRequest.js
    requestEditCommentForm(reCommentItemContainer,comment);
    editButtonHideControl(reCommentItemContainer);
}

//서버로 보낼 데이터 정제
function getCommentData(commentItemContainer, type) {
    const postId = document.querySelector('.quiz-id').value;
    const userId = commentItemContainer.querySelector('.comment-writer-name').value;
    let commentContent;
    let parentCommentId;

    if(type==="comment"){
        parentCommentId=0;
        commentContent=commentItemContainer.querySelector('.comment-item');
    }else{
        parentCommentId=1;
        commentContent=commentItemContainer.querySelector('.comment-item-body');
    }

    const contents = commentContent.textContent;
    console.log(contents);
    const display = commentContent.classList.contains('hide-text');
    console.log(display);

    return {
        userId: userId,
        postId: postId,
        contents: contents,
        parentCommentId: parentCommentId,
        display: display,
    };
}

//수정, 삭제 버튼 사라지게 함
function editButtonHideControl(commentItemContainer){
    const commentEditContainer= commentItemContainer.querySelector('.comment-edit-container');
    commentEditContainer.classList.toggle('hidden');
}