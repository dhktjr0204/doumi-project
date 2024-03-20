function clickToGoLeaveMembership(event) {
  event.preventDefault();

  const isConfirmed = confirm("Bend Doumi에서 탈퇴하시겠습니까?");

  if (isConfirmed) {
    // 사용자 ID 가져오기
    let id = document.getElementById('myPage_userName').getAttribute(
        'data-userid');

    fetch(`/user/${id}/delete`, {
      method: 'DELETE',
    })
    .then(response => {
      if (response.ok) {
        alert("탈퇴 처리가 완료되었습니다.");
        window.location.replace('/user/login');
      } else {
        alert("탈퇴 처리 중 오류가 발생했습니다.");
      }
    })
    .catch(error => {
      console.error('탈퇴 요청 실패:', error);
      alert("탈퇴 처리 중 문제가 발생했습니다.");
    });
  } else {
    alert("탈퇴 절차가 취소되었습니다.");
  }
}
