document.addEventListener('DOMContentLoaded', function () {
  document.getElementById('linkQuestions').addEventListener('click',
      function (e) {
        e.preventDefault(); // a태그가 링크로 이동하는 기본동작을 방지

        //아래의 링크를 변경해야 한다.임시로 /user/codingtest/post로 설정
        fetch(`/user/codingtest/post`)
        .then(response => response.json()) // 서버로부터 받은 JSON 응답을 파싱
        .then(data => {
          let container = document.querySelector('.quiz_container');

          container.innerHTML = ''; // 기존 내용을 지움

          data.forEach(cote => {
            // 서버에서 받은 cote 데이터로 html을 생성한다.
            let item = `<li class="quiz_container">
                                <div class="sequence">${cote.id}</div>
                                <div class="title">
                                    <a href="/quiz/board?id=${cote.id}" target="_blank">${cote.title}</a>
                                </div>
                                <div class="author">${cote.userId}</div>
                                <div class="create_time">${cote.createdAt}</div>
                                <div class="likes">${cote.likeCount}</div>
                            </li>`;
            container.innerHTML += item; // 생성한 HTML을 container에 추가
          });
        })
        .catch(error => console.log('Error:', error));
      });
});
