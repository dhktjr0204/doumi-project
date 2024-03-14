function clickToGoMyCodingtestPost() {
  let id = document.getElementById('myPage_userName').getAttribute(
      'data-userid');
  switchHighlight('coteType');
  $.ajax({
    //JS에 백틱을 써서 템플릿 리터럴을 사용했다
    url: `/user/${id}/codingtest/posts`,
    type: "GET",
    success: function (data) {
      $('.show_user_wrote .table_container .postList').html(data);
      console.log(data);
    },
    error: function (error) {
      console.log(error);
    }
  });
}

function clickToGoMyQuizPost() {
  let id = document.getElementById('myPage_userName').getAttribute(
      'data-userid');
  switchHighlight('quizType');
  $.ajax({
    url: `/user/${id}/quiz/posts`,
    type: "GET",
    success: function (data) {
      $('.show_user_wrote .table_container .postList').html(data);
      console.log(data);
    },
    error: function (error) {
      console.log(error);
    }
  });
}

function clickToGoMyCommentPost() {
  let id = document.getElementById('myPage_userName').getAttribute(
      'data-userid');
  switchHighlight('commentType');
  $.ajax({
    url: `/user/${id}/comment/posts`,
    type: "GET",
    success: function (data) {
      $('.show_user_wrote .table_container .postList').html(data);
      console.log(data);
    },
    error: function (error) {
      console.log(error);
    }
  });
}

function switchHighlight(selectedId) {
  let items = ['coteType', 'quizType', 'commentType'];

  items.forEach(function (id) {
    document.getElementById(id).style.color = ''; // 원래 색상으로 초기화
  });
  
  document.getElementById(selectedId).style.color = '#04B45F';
}
