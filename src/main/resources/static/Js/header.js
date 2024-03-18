window.addEventListener('scroll', function () {
  let header = document.querySelector(".box-header");
  if (window.scrollY > 0) {
    header.classList.add('shadow');
  } else {
    header.classList.remove('shadow');
  }
});