async function loadOptions() {
    const response = await fetch('http://localhost:8080/options');
    const data = await response.json();

    for (const key in data) {
        const select = document.getElementById(key);
        data[key].forEach(opt => {
            const option = document.createElement('option');
            option.value = opt.name;
            option.text = opt.name + (opt.stock === false ? " (Out of stock)" : "");
            option.disabled = opt.stock === false;
            select.appendChild(option);
        });
    }
}

async function addToCart() {
    const formData = {
        frameType: document.getElementById('frameType').value,
        frameFinish: document.getElementById('frameFinish').value,
        wheels: document.getElementById('wheels').value,
        rimColor: document.getElementById('rimColor').value,
        chain: document.getElementById('chain').value
    };

    const response = await fetch('http://localhost:8080/cart', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(formData)
    });

    const result = await response.json();
    document.getElementById('price').innerText = result.price;
}

loadOptions();
