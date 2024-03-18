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
    .then(response => response.json())
    .then(data => {
      if (data.success) { //로그인을 성공하면
        window.location.replace('/'); //메인페이지로 리다이렉트
      } else {//로그인을 실패하면
        alert(data.errormsg);//errormsg를 클라이언트에게 알려준다.
        window.location.replace('/user/login');
      }
    })
    .catch(error => console.error('Error:', error));
  });
});