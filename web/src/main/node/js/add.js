/**
 * Created by piotrek on 6/20/15.
 */
$(function () {
    $('#add-keyword-button').click(addKeyword);
    function addKeyword() {
        $.post('http://localhost:8080/keywords', {
            value: $('#add-keyword-input').val()
        }, function (data) {
            $('#add-keyword-input').val('');
        });
    }

    $('#add-connection-button').click(addConnection);
    function addConnection() {
        $.post('http://localhost:8080/keywords/connections', {
            firstKeyword: $('#add-first-keyword-input').val(),
            secondKeyword: $('#add-second-keyword-input').val()
        }, function (data) {
            $('#add-first-keyword-input').val('');
            $('#add-second-keyword-input').val('');
        });
    }
});