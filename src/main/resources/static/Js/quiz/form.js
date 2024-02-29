// 과목별로 세무과목 바꿔주기
const subjectTagButtons=document.querySelectorAll('.subject-tag-button');
const detailTagsContainer = document.querySelectorAll('.detail-tags');

//과목 클릭 시 그 과목에 해당하는 세부과목 보여주기
subjectTagButtons.forEach((button)=>{
    button.addEventListener('click',()=>{
        showDetailTagButtons(button);
    })
});

//세부과목 바꿔주는 메서드
function showDetailTagButtons(subject){
    // 클릭한 과목 꺼내오기
    const subjectName = subject.textContent.toLowerCase();
    const selectedTagsContainer = document.querySelector(`.${subjectName}`);

    // 모든 detailTagsContainer에 hidden 클래스 추가
    detailTagsContainer.forEach(tag => tag.classList.add('hidden'));

    // 클릭된 버튼과 이름이 동일한 세부과목 div에 hidden 제거
    selectedTagsContainer.classList.remove('hidden');

    //선택된 과목 바꿔주기(border-bottom에 흰 선이 생긴다)
    subjectTagButtons.forEach((button)=>{
        button.classList.remove('selected-subject');
    })
    subject.classList.add('selected-subject');
}

//태그 선택하기
const detailsTagButtons = document.querySelectorAll('.detail-tag-button');
const selectedTagsContainer = document.querySelector('.selected-tags');

//세부과목 버튼 누를때마다 색깔 바뀜, 태그 생성 기능
detailsTagButtons.forEach(button => {
    button.addEventListener('click', () => {
        // 세부과목 버튼 색깔 바뀜
        button.classList.toggle('selected');
        //태그 추가 / 제거
        if (button.classList.contains('selected')) {
            const selectedTag=createSelectedTag(button);
            // 생성된 태그를 '선택된 태그들:'뒤에 태그 추가
            selectedTagsContainer.appendChild(selectedTag);
        } else {
            const buttonValue=button.value;
            // 태그 버튼을 한 번 더 눌렀을 경우 삭제
            const removeTag = document.querySelector(`.selected-tag[value="${buttonValue}"]`);
            selectedTagsContainer.removeChild(removeTag);
        }
    });
});

// 선택한 태그 생성 메서드
function createSelectedTag(button){
    const buttonText = button.textContent;
    const selectedTag = document.createElement('button');
    selectedTag.classList.add('selected-tag');
    selectedTag.textContent = buttonText;
    selectedTag.value = button.value;

    return selectedTag;
}

//선택한 태그 삭제
selectedTagsContainer.addEventListener('click',(event)=>{
    let clickElement = event.target;
    if(clickElement.classList.contains('selected-tag')){
        //삭제할 태그와 같은 이름의 태그 버튼 가져오기
        const detailTag=document.querySelector(`.detail-tag-button[value="${clickElement.value}"]`);
        //버튼 색깔 원상복구
        detailTag.classList.toggle('selected');
        //선택된 태그 삭제
        clickElement.remove();
    }
})

//초기화
const resetButton = document.querySelector('.reset-button');
resetButton.addEventListener('click',()=>{
    // 여기서 부터 수정 필요
    selectedTagsContainer.innerHTML="";
    detailsTagButtons.forEach((button)=>{
        button.classList.remove('selected');
    })
})

// 취소 버튼 클릭
const cancelButton = document.querySelector('.cancel-button');
cancelButton.addEventListener('click', () => {
    // 취소
    location.href = '/quiz';
});


//글자수 제한 길이
const byteLimit = 3000;
const titleByteLimit = 250;

//제목 칸 늘리기
function autoResize(textarea) {
    //height초기화
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight+ 'px';
}

//textarea 실시간 값 변경 감지하기
const titleTextCount=document.querySelector('.title-text-count');
$(document).ready(function() {
    $('.title').on('change input', function() {
        // Assuming countBytes is a function that you have defined elsewhere
        countBytes(this, titleTextCount, titleByteLimit);
        autoResize(this);
    });
});
const title= document.querySelector('.title');
countBytes(title,titleTextCount,titleByteLimit);