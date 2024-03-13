function clickToGoMyCodingtestPost() {
  let id = document.getElementById('myPage_userName').getAttribute(
      'data-userid');

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

  $.ajax({
    url: "/user/${id}/comment/posts",
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