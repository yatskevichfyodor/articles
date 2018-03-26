$('#pdf-btn').click(function(){
    var element = document.getElementById('pdf');

    html2pdf(element, {
        margin:       1,
        filename:     $('.header').text() + '.pdf',
        image:        { type: 'jpeg', quality: 0.98 },
        html2canvas:  { dpi: 192, letterRendering: true },
        jsPDF:        { unit: 'in', format: 'letter', orientation: 'portrait' }
    });
});
