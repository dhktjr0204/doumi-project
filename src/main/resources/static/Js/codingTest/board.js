function clickLike(button, type) {

    const likeImage = button.querySelector('img');
    const likeNumber = button.querySelector('.likes-number');

    likeImage.classList.toggle('liked');

    let url = likeImage.classList.contains('liked') ? '/like/add' : '/like/cancel';
    let postId = button.querySelector('.post-id').value;

    $.ajax({
        type: "GET",
        url: url,
        data: {
            postId: postId,
            type: type
        },
        success: function(response) {

            likeNumber.textContent = response;
        },
        error: function(error) {

            console.error(error);
        }
    });
}

// 대댓글 숨기기 기능
function updateHideButton(reCommentItemsContainer, button) {
    //대댓글 div안에 있는 댓글들
    const recommentItems = reCommentItemsContainer.querySelectorAll('.re-comment-item-container');
    let count = recommentItems.length;

    if (reCommentItemsContainer.classList.contains('hidden')) {
        button.innerHTML = count + "개 답변";
    } else {
        button.innerHTML = `<img src="/images/icon_up_arrow.png" alt="숨기기 버튼"> 숨기기`;
    }
}

//숨기기 버튼 눌렀을 때
function clickHideReComment(button) {
    //closest 메서드를 사용하여 각 버튼에 가장 가까운 .comment-item-container를 찾고, 해당 컨테이너 내부에서 .re-comment-items-container를 선택
    const reCommentItemsContainer = button.closest('.comment-item-container').querySelector('.re-comment-items-container');
    //대댓글이 하나도 없을 수도 있다.
    // 대댓글 div hidden
    reCommentItemsContainer.classList.toggle("hidden");
    updateHideButton(reCommentItemsContainer, button);
}

/*// 좋아요 개수 늘리기, 이미지 바꾸기 메서드
function likeControl(likeImage, likeNumber) {
    // 따봉 이미지 바꾸기
    likeImage.classList.toggle('liked');

    if (likeImage.classList.contains('liked')) {
        likeNumber.textContent = parseInt(likeNumber.textContent) + 1;
    } else {
        likeNumber.textContent = parseInt(likeNumber.textContent) - 1;
    }
}

// 좋아요 누르기
function clickLike(button){
    const likeNumber = button.querySelector('.likes-number');
    const likeImage = button.querySelector('img');
    likeControl(likeImage, likeNumber);
}*/

// 댓글 정답 보기
function clickComment(commentContainer){
    commentContainer.classList.toggle('hide-place');
    const comment = commentContainer.querySelector('.comment-item');
    comment.classList.toggle('hide-text');
}

//정답 댓글 작성
// 홍길동님,답변을 작성해보세요를 누르면 댓글 입력칸이 나온다.
function clickCommentEditorOpener(commentEditorOpener){
    const commentEditor = document.querySelector('.comment-editor');
    commentEditorOpener.classList.toggle('hidden');
    commentEditor.classList.toggle('hidden');
}

//대댓글 작성
function clickReCommentEditorOpener(button){
    let commentContainer = button.closest('.comment-item-container');
    const reCommentEditor = commentContainer.querySelector('.re-comment-editor');
    reCommentEditor.classList.toggle('hidden');
}


// 취소 버튼을 누르면 사라진다.
function clickCommentCancel(){
    const commentEditorOpener = document.querySelector('.comment-editor-opener');
    const commentEditor = document.querySelector('.comment-editor');
    commentEditorOpener.classList.toggle('hidden');
    commentEditor.classList.toggle('hidden');
}

//대댓글 취소 버튼 사라짐
function clickReCommentCancel(button){
    const reCommentEditor = button.closest('.re-comment-editor');
    reCommentEditor.classList.toggle('hidden');
}


// 좋아요순 최신순 클릭
const recommend = document.querySelector('.recommend');
const recent = document.querySelector('.recent');
recommend.addEventListener('click', () => {
    recommend.classList.toggle('sorted');
    recent.classList.toggle('sorted');
})
recent.addEventListener('click', () => {
    recent.classList.toggle('sorted');
    recommend.classList.toggle('sorted');
})

function deleteContent(){
    const confirmed= window.confirm("정말 삭제하시겠습니까?");
    let userId = document.querySelector('.board-writer-name').getAttribute("value");
    let postId = document.querySelector(".post-id").value;
    if(confirmed) {
        $.ajax({
            type: 'DELETE',
            url: "/codingtest/delete?postId="+postId+"&userId="+userId,
            contentType: false,
            processData: false,
            success: function (redirectUrl) {
                location.replace(redirectUrl);
            },
            error: function (error) {
                if (error.status === 400) {
                    alert("Bad Request: "+ error.responseText);
                }else if(error.status===401){
                    alert("Unauthorized: "+error.responseText);
                    location.replace("/codingtest/index");
                }else{
                    alert("error: "+error.responseText);
                }
            }
        });
    }else{
        alert("삭제가 취소 되었습니다.");
    }
}

function editContent(){
    let postId = document.querySelector(".post-id").value;
    location.replace("/codingtest/edit?id="+postId);
}