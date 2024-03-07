document.addEventListener('DOMContentLoaded', function () {
  const signupForm = document.querySelector('.signup_form');
  signupForm.addEventListener('submit', function (event) {
    event.preventDefault(); // 기본 제출 동작 방지

    const id = document.getElementById('id').value;
    const password = document.getElementById('password').value;
    const passwordConfirm = document.getElementById('passwordConfirm').value;

    // 닉네임 유효성 검사
    const idRegex = /^[a-zA-Z0-9]{4,20}$/;
    if (!idRegex.test(id)) {
      alert('아이디는 최소 4글자 이상 최대 20자 이하이며 영문자와 숫자만 가능합니다.');
      return;
    }

    // 비밀번호 유효성 검사
    // const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{4,20}$/;
    if (!passwordRegex.test(password)) {
      // alert(
      //     '비밀번호는 최소 8자 이상 최대 20자 이하, 하나 이상의 대문자, 하나의 소문자, 하나의 숫자, 하나의 특수 문자를 포함해야 합니다.');
      alert('비밀번호는 최소 4자 이상 최대 20자 이하, 하나 이상의 문자와 하나의 숫자를 포함해야 합니다.');
      return;
    }

    // 비밀번호와 비밀번호 확인 일치 검사
    if (password !== passwordConfirm) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }

    // 여기에서 서버로의 폼 제출 로직을 구현할 수 있음
    fetch('/user/signup', {
      method: 'POST', //HTTP 요청 메서드
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({//JS객체를 JSON문자열로 변환한다
        id: id,
        password: password,
      })
    })
    .then(response => {
      return response.json();
    })//서버에서 받은 객체를 json으로 변환한다
    .then(data => {
      if (data.success) {//회원가입 성공
        alert(data.message);
        window.location.href = '/';
      } else {//회원가입 실패
        alert(data.errormsg);
      }
    })
    .catch(error => console.error('Error:', error));
  });
});