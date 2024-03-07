const submitButton = document.querySelector('.submit-button');
// 등록 버튼 클릭
submitButton.addEventListener('click', () => {
    handleSubmit("/codingtest/post");
});

const editButton = document.querySelector('.edit-button');
editButton.addEventListener('click', () => {
    let postId = document.querySelector('.post-id').value;
    handleSubmit('/codingtest/edit?id=' + postId);
});

function handleSubmit(url) {
    const title = document.querySelector('.title').value.trim();

    // 타이틀 또는 coteContent가 비어 있는 경우 알림창을 띄우고 폼을 제출하지 않음
    if (!title && !contentEditor.getMarkdown()) {
        alert('제목과 내용을 작성해주세요.');
        return;
    } else if (!title) {
        alert('제목을 입력해주세요.');
        return;
    } else if (!contentEditor.getMarkdown()) {
        alert('내용을 입력해주세요.');
        return;
    }
    // 폼 데이터에 새로 생성된 태그 값들을 추가하는 JavaScript 코드 추가
    const formData = new FormData(document.querySelector('.coteForm'));
    formData.append('coteContent', contentEditor.getMarkdown());
    // 폼 검증 통과 시
    // 폼 데이터를 서버로 전송
    $.ajax({
        type: 'POST',
        url: url,
        data: formData,
        contentType: false,
        processData: false,
        success: function (redirectUrl) {
            location.href = redirectUrl;
        },
        error: function (error) {
            if (error.status === 400) {
                alert("Bad Request: "+ error.responseText);
            }else if(error.status===401){
                alert("Unauthorized: "+error.responseText);
                location.href="/codingtest/index";
            }else{
                alert("error: "+error.responseText);
            }
        }
    });
}

// 취소 버튼 클릭
const cancelButton = document.querySelector('.cancel-button');
cancelButton.addEventListener('click', () => {
    // 취소
    location.href = '/codingtest/index';
});