function clickCommentSubmitButton(button){
    const commentForm=button.closest('.comment-form');
    submitComment(commentForm);
}

function clickReCommentSubmitButton(button){
    const commentForm=button.closest('.re-comment-form');
    submitComment(commentForm);
}

function clickCommentEditButton(button){
    const commentForm=button.closest('.comment-form');
    const commentItemContainer= button.closest('.re-comment-item-container') || button.closest('.comment-item-container');
    const commentId= commentItemContainer.querySelector('.comment-id').value;
    editComment(commentForm, commentId);
}

//삭제 버튼
function  clickDeleteButton(button){
    const confirmed= window.confirm("정말 삭제하시겠습니까?");

    if(confirmed) {
        const postId = document.querySelector('.post-id').value;
        const commentItemContainer = button.closest('.re-comment-item-container') || button.closest('.comment-item-container');
        const commentId = commentItemContainer.querySelector('.comment-id').value;
        const userId=commentItemContainer.querySelector('.comment-writer-name').getAttribute("value");
        deleteComment(postId, commentId,userId);
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


function submitComment(commentForm) {
    const comment=commentForm.querySelector('.answer-input').value;
    if (!comment) {
        alert('댓글을 작성해주세요.');
        return;
    }

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
            errorHandler(error);
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
            errorHandler(error);
        }
    });
}

function editComment(commentForm,commentId){
    const comment=commentForm.querySelector('.answer-input').value;
    if (!comment) {
        alert('댓글을 작성해주세요.');
        return;
    }

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
            errorHandler(error);
        }
    });
}

function deleteComment(postId, commentId, userId){
    $.ajax({
        url: "/comment/delete?postId="+postId+"&commentId="+commentId+"&userId="+userId,
        type: "DELETE",
        processData: false,
        contentType: false,
        success: function (data) {
            $('.comment-container').html(data);
        },
        error: function (error) {
            errorHandler(error)
        }
    });
}

function errorHandler(error){
    if (error.status === 400) {
        alert("Bad Request: "+ error.responseText);
    }else if(error.status===401){
        alert("Unauthorized: "+error.responseText);
        location.reload();
    }else{
        alert("error: "+error.responseText);
    }
}
