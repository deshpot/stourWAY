(function($) {

  // Define the hello button
  $.cleditor.buttons.qzsituimage = {
    name : "qzsituimage",
    image : "qzsituimage.gif",
    title : "插入图片",
    command : "inserthtml",
    popupName : "qzsituimage",
    popupClass : "cleditorPrompt",
    popupContent : "选择图片:<br><form action=\"/file/upload\" id=\"imageform\" name=\"imageform\" encType=\"multipart/form-data\" "
        + " method=\"post\" target=\"hidden_frame\" ><input type=\"file\" id=\"imageFile\" name=\"imageFile\"><br>"
        + "<input type=button value=Submit><iframe name=\'hidden_frame\' id=\"hidden_frame\" style='display:none'>"
        + "</iframe></form>",
    buttonClick : qzsituImageClick
  };

  // Add the button to the default controls before the bold button
  $.cleditor.defaultOptions.controls = $.cleditor.defaultOptions.controls
      .replace("bold", "qzsituimage bold");

  // Handle the hello button click event
  function qzsituImageClick(e, data) {
    // Wire up the submit button click event
    $(data.popup).find(":button").unbind("click").bind("click", function(e) {
      $('#imageform').submit();
      // Get the editor
      //var editor = data.editor;

      // Insert some html into the document
      //var html = "Hello " + name;
      //editor.execCommand(data.command, html, null, data.button);

      // Hide the popup and set focus back to the editor
      //editor.hidePopups();
      //editor.focus();
    });
  }
})(jQuery);
