const quizTextCount = document.querySelector('.quiz-text-count');

const contentEditor = new toastui.Editor({
    el: document.querySelector('.cote-content'), // 에디터를 적용할 요소 (컨테이너)
    height: '800px',                        // 에디터 영역의 높이 값 (OOOpx || auto)
    initialEditType: 'markdown',            // 최초로 보여줄 에디터 타입 (markdown || wysiwyg)
    initialValue: coteContent,                       // 내용의 초기 값으로, 반드시 마크다운 문자열 형태여야 함
    previewStyle: 'vertical',               // 마크다운 프리뷰 스타일 (tab || vertical)
    placeholder: '질문을 작성해주세요',
    autofocus: false,
    events: {
        change: function () {
            //wordCount.js 파일
            countBytes(contentEditor, quizTextCount, byteLimit);
        }
    },

    hooks: {

        async addImageBlobHook(blob, callback) {
            try{
                const formData = new FormData();
                formData.append('file', blob);

                const response = await fetch('/image-upload', {
                    method : 'POST',
                    body : formData,
                });

                const fileName = await response.text();
                const imageUrl = `/image-print?fileName=${fileName}`;

                callback(imageUrl, 'image alt attribute');

            } catch (error) {
                console.error('파일 업로드 실패 : ', error);
            }
        }
    }
});

countBytes(contentEditor, quizTextCount, byteLimit);