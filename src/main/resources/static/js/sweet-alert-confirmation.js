
document.addEventListener('DOMContentLoaded', function() {
	document.querySelectorAll('.btn-eliminar').forEach(btn => {
		btn.addEventListener('click', function(e) {
			e.preventDefault();
			
			const title = this.getAttribute('data-title');
			const text = this.getAttribute('data-text');
			const icon = this.getAttribute('data-icon');
			console.log(icon);
			
			Swal.fire({
				title: title,
				text: text, 
				icon: icon, 
				showCancelButton: true,
				confirmButtonColor: '#d33',
				cancelButtonText: 'Cancelar',
				confirmButtonText: 'Sí'
			}).then(result => {
				if (result.isConfirmed) {
					const id = this.getAttribute("data-id");
				    const form = document.getElementById("formEliminar-" + id);
					
					form.submit();
				}
			});
		});
	});
});
