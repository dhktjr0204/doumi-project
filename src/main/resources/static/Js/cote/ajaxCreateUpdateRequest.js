const submitButton = document.querySelector('.submit-button');
// 등록 버튼 클릭
submitButton.addEventListener('click', () => {
    handleSubmit("/codingtest/post");
});

const editButton=document.querySelector('.edit-button');
editButton.addEventListener('click',()=>{
    let postId = document.querySelector('.quiz-id').value;
    handleSubmit('/codingtest/edit?id='+postId);
});

function handleSubmit(url){
    const title = document.querySelector('.title').value.trim();
    const selectedTags = document.querySelectorAll('.selected-tag');
    const tagValues = Array.from(selectedTags).map(tag => tag.value);
    // 타이틀 또는 coteContent가 비어 있는 경우 알림창을 띄우고 폼을 제출하지 않음
    if (!title || !contentEditor.getMarkdown()) {
        alert('퀴즈 제목과 내용을 작성해주세요.');
        return;
    }
    // 폼 데이터에 새로 생성된 태그 값들을 추가하는 JavaScript 코드 추가
    const formData = new FormData(document.querySelector('.coteForm'));
    formData.append('tags', tagValues.join(','));
    formData.append('coteContent', contentEditor.getMarkdown());
    formData.append('answerContent', answerEditor.getMarkdown());
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
            console.error(error);
        }
    });
}