$(document).ready(function() {
  gotoPage("/todo/infoCenter");
//  $(window).bind('hashchange', function() {
//    if(flagA == 1) {flagA = 0; return;}
//    flagB = 1;
//    gotoPage(location.hash.replace("#",""));
//    flagB = 0;
//  });
});

//var flagA = 0;
//var flagB = 0;

var submitForm = function(formObj) {
  $(formObj).ajaxSubmit(function(data) {
      $("#mainDiv").html(data);
  });
};

var gotoPage = function(url, parameters) {
//  flagA = 1;
//  if(flagB == 0)
//    location.hash = "#"  + url;
  $.ajax({
    type : "post",
    url : url,
    data : parameters,
    dataType : "html",
    success : function(data) {
      $("#mainDiv").html(data);
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      alert(errorThrown);
    }
  });
};

var loadPage = function(container, url, parameters) {
  $.ajax({
    type : "post",
    url : url,
    data : parameters,
    dataType : "html",
    success : function(data) {
      $(container).html(data);
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      alert(errorThrown);
    }
  });
};

var gotoPageWithObj = function(url, parametersObject) {
  $.ajax({
    type : "post",
    url : url,
    data : JSON.stringify(parametersObject),
    dataType : "html",
    contentType : 'application/json;charset=UTF-8',
    success : function(data) {
      $("#mainDiv").html(data);
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      alert(errorThrown);
    }
  });
};