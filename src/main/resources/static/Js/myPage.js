function clickToGoMyCodingtestPost() {
  $.ajax({
    url: "/user/codingtest/post",
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
  $.ajax({
    url: "/user/quiz/post",
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
  $.ajax({
    url: "/user/comment/post",
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