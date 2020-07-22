var button = document.getElementById("reply-button");

button.addEventListener('click', e => {
    var body = button.parentNode.parentNode;

    console.log(body);

    var formBox = document.createElement("form");
    formBox.setAttribute("action", "#");

    var container = document.createElement("DIV");
    container.setAttribute("class", "container");

    var reply_title = document.createElement("P");
    reply_title.setAttribute("class", "reply-title");
    reply_title.innerText = "Your reply";

    var comment_box = document.createElement("DIV");
    comment_box.setAttribute("class", "card comment-box");

    var form_group = document.createElement("DIV");
    form_group.setAttribute("class", "form-group");

    var reply_box_label = document.createElement("LABEL");
    reply_box_label.setAttribute("for", "reply-box");
    reply_box_label.innerText = "Reply:";

    var reply_box_textarea = document.createElement("TEXTAREA");
    reply_box_textarea.setAttribute("id", "reply-box");
    reply_box_textarea.setAttribute("class", "form-control");
    reply_box_textarea.setAttribute("aria-label", "With textarea");
    reply_box_textarea.setAttribute("placeholder", "Enter your message...");

    var reply_button = document.createElement("INPUT");
    reply_button.setAttribute("type", "submit");
    reply_button.setAttribute("class", "btn btn-info btn-primary");
    reply_button.innerText = "Reply";

    form_group.appendChild(reply_box_label);
    form_group.appendChild(reply_box_textarea);
    comment_box.appendChild(form_group);
    container.appendChild(reply_title);
    container.appendChild(comment_box);
    container.appendChild(reply_button);
    formBox.appendChild(container);
    body.appendChild(formBox);


    button.parentNode.removeChild(button);


})