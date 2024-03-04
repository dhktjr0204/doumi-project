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
autoResize(title);