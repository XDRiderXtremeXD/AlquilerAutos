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




document.addEventListener("DOMContentLoaded", function () {
	const marcaSelect = document.getElementById("marcaSelect");
	const vehiculoSelect = document.getElementById("vehiculoSelect");
	const precioInput = document.getElementById("precioDiaInput");

	// Al cambiar de marca, actualizar la lista de vehículos
	marcaSelect.addEventListener("change", function () {
		const idMarca = this.value;
		vehiculoSelect.innerHTML = '<option value="">Seleccione un vehículo</option>';
		precioInput.value = '';

		if (idMarca) {
			fetch('/alquiler/vehiculosPorMarca?idMarca=' + idMarca)
				.then(response => response.json())
				.then(data => {
					data.forEach(v => {
						const option = document.createElement("option");
						option.value = v.id;
						option.text = v.placa + " - " + v.modelo;
						option.setAttribute("data-precio", v.precioXDia);
						vehiculoSelect.appendChild(option);
					});
				});
		}
	});

	vehiculoSelect.addEventListener("change", function () {
		const selectedOption = vehiculoSelect.options[vehiculoSelect.selectedIndex];
		const precio = selectedOption.getAttribute("data-precio");
		precioInput.value = precio || '';
		calcularTotal();
	});

	function calcularTotal() {
		const precioDia = parseFloat(document.getElementById('precioDiaInput').value) || 0;
		const fechaInicio = new Date(document.getElementById('fechaPrestamoInput').value);
		const fechaFin = new Date(document.getElementById('fechaDevolucionInput').value);
		const abono = parseFloat(document.getElementById('abonoInput').value) || 0;

		if (fechaInicio && fechaFin && !isNaN(precioDia)) {
			const diffTime = fechaFin - fechaInicio;
			const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
			const total = (diffDays) * precioDia - abono;
			document.getElementById('totalPagarInput').value = total.toFixed(2);
		}
	}

	document.getElementById('fechaPrestamoInput').addEventListener('change', calcularTotal);
	document.getElementById('fechaDevolucionInput').addEventListener('change', calcularTotal);
	document.getElementById('abonoInput').addEventListener('input', calcularTotal);
});

