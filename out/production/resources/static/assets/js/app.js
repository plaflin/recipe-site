//confirm password
$('#sign-up').click(function() {
    var pass1 = $('#password').val();
    var pass2 = $('#confirm-password').val();
    var message = '<p id="message" class="error-message">The password and password confirmation are not identical</p>'

    if (pass2 != pass1) {
        $('#confirm-password')
            .addClass('error')
            .after(message);
            return false;
    } else {
        $('#confirm-password').removeClass('error');
        $('message').remove();
    }
}
)

//Add step row
$('#add-step').click(function() {
    var index = $('.step-row').length;

    var newRow = '<div class="step-row">' +
                        '<div class="prefix-20 grid-80">' +
                             '<p>' +
                                 '<input  type="text" id="steps' + index + '.stepName" name="steps[' + index + '].stepName" />' +
                             '</p>' +
                        '</div>' +
                    '</div>'

    $("#add-step-row" ).before(newRow);
});

//Add ingredient row
$('#add-ingredient').click(function() {
    var index = $('.ingredient-row').length;

    var newRow = '<div class="ingredient-row">' +
                        '<div class="prefix-20 grid-30">' +
                            '<p> <input type="text" id="ingredients' + index + '.item" name="ingredients[' + index + '].item" />' +
                            '</p> </div>' +
                        '<div class="grid-30">' +
                            '<p> <input type="text" id="ingredients' + index + '.condition" name="ingredients[' + index + '].condition" />' +
                            '</p> </div>' +
                        '<div class="grid-10 suffix-10">' +
                             '<p> <input type="text" id="ingredients' + index + '.quantity" name="ingredients[' + index + '].quantity" />' +
                             '</p> </div>'
                    '</div>'

    $("#add-ingredient-row" ).before(newRow);
});

//Filter by category
$('#select-category').change(function() {
    var category = $('#select-category').val();
    window.location.href = '/recipes/category/' + category;
});

//search on Enter
$('#search').on('keypress', function (e) {
    if(e.which === 13){
    $(this).attr("disabled", "disabled");
    var search = $('#search').val();
    var searchMode = $('#search-mode').val();
    window.location.href = '/recipes/search?' + searchMode + '=' + search;
    }
});