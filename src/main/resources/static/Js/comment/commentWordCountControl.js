function commentWordCount(textarea){
    const commentForm= textarea.closest('.comment-form');
    const commentTextCount = commentForm.querySelector('.comment-text-count');
    countBytes(textarea, commentTextCount, 1500);
}

function reCommentWordCount(textarea){
    const reCommentForm= textarea.closest('.re-comment-form');
    const reCommentTextCount = reCommentForm.querySelector('.re-comment-text-count');
    countBytes(textarea, reCommentTextCount, 1500);
}

function  commentEditFormWordCount(commentItemContainer){
    const textarea= commentItemContainer.querySelector('.answer-input');
    const commentTextCount = commentItemContainer.querySelector('.comment-text-count');
    countBytes(textarea, commentTextCount, 1500);
}