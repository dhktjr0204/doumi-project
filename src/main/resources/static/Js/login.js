document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.querySelector('.login_form');
  loginForm.addEventListener('submit', function (event) {
    event.preventDefault(); // 기본 제출 동작 방지

    const id = document.getElementById('id').value;
    const password = document.getElementById('password').value;

    //모든 필드가 채워져있는지 확인
    if (!id || !password) {
      alert('모든 필드를 입력해주세요.');
      return;
    }

    //서버로 아이디와 비밀번호를 보내 검증
    fetch('/user/login', {
      method: 'POST',
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: `id=${encodeURIComponent(id)}&password=${encodeURIComponent(
          password)}`
    })
    .then(response => response.json())//서버에서 받은 객체를 json으로 변환한다
    .then(data => {
      if (data.success) { //로그인을 성공하면
        window.location.href = '/'; //메인페이지로 리다이렉트
      } else {
        alert(data.message);
        window.location.href = '/user/login';
      }
    })
    .catch(error => console.error('Error:', error));
  });
});