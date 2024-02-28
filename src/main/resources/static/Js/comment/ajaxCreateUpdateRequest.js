function clickCommentSubmitButton(button){
    const commentForm=button.closest('.comment-form');
    submitCommentForm(commentForm);
}

function clickReCommentSubmitButton(button){
    const commentForm=button.closest('.re-comment-form');
    submitCommentForm(commentForm);
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
            console.log(data);
            $(commentItemContainer).children('.comment-item-body').replaceWith(data);
            commentEditFormWordCount(commentItemContainer);
        },
        error: function (error) {
            console.log(error);
        }
    });
}
