function llenarModalEditar(btn) {
	const datos = btn.dataset; // obtiene todos los atributos data-*
	console.log(datos);
	//Hace un for de todos los atributos mandados y modifica los elementos de editar de algun formulario de editar con el nombre "editar"+ATRIBUTO				
	for (let key in datos) {
		if (key.startsWith("bs")) continue; // ignorar atributos Bootstrap como bsToggle, bsTarget
		const input = document.getElementById("editar" + key.charAt(0).toUpperCase() + key.slice(1));
		if (input) {
			input.value = btn.getAttribute("data-" + key);
		}
	}

	if (datos.itemhabilitar) {
		const input = document.getElementById("editar" + datos.itemhabilitar.charAt(0).toUpperCase() + datos.itemhabilitar.slice(1));
		if (input && datos.condhabilitado != null) 
			input.disabled = (datos.condhabilitado === "true");
	}
}

function borrarImagen() {
	inputFoto.value = '';
	minFoto.src = '';
	contFoto.style.display = 'none';
}

function borrarImagenEdit() {
	inputFotoEdit.value = '';
	minFotoEdit.src = '';
	contFotoEdit.style.display = 'none';
	document.getElementById('alertaFotoEdit').classList.add('d-none');
}






