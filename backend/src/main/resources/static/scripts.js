const endpoints = {
    switchState: '/switchState',
    switchOn: '/switchOn',
    switchOff: '/switchOff',
    clockIn: '/clockIn',
    checkStatus: '/check',
    clockOut: '/clockOut'
};

document.querySelectorAll('.btn').forEach(button => {
    button.addEventListener('click', async () => {
        // Disable all buttons
        document.querySelectorAll('.btn').forEach(btn => {
            btn.disabled = true;
            btn.style.cursor = 'not-allowed';
        });

        // Show overlay
        const overlay = document.getElementById('overlay');
        overlay.style.display = 'flex';
        overlay.textContent = 'Waiting for server response...';

        if (button.id === 'btn1') {
            displayMessage(await sendPutRequest(endpoints.clockIn));
        } else if (button.id === 'btn2') {
            displayMessage(await sendGetRequest(endpoints.checkStatus));
        } else if (button.id === 'btn3') {
            displayMessage(await sendPutRequest(endpoints.clockOut));
        }

        // Simulate server response delay
        function displayMessage(message) {
            overlay.textContent = message;
            setTimeout(() => {
                overlay.style.display = 'none';
                document.querySelectorAll('.btn').forEach(btn => {
                    btn.disabled = false;
                    btn.style.cursor = 'pointer';
                });
            }, 5000);
        }
    });
});

const toggleSwitch = document.getElementById('toggleSwitch');

document.addEventListener('DOMContentLoaded', async () => {
    // Function to set the switch state based on backend response
    const setSwitchState = async () => {
        toggleSwitch.checked = await sendGetRequest(endpoints.switchState) === true;
    };

    // Initial call to set the switch state
    await setSwitchState();
});

toggleSwitch.addEventListener('change', async () => {
    if (toggleSwitch.checked) {
        await sendPutRequest(endpoints.switchOn);
    } else {
        await sendPutRequest(endpoints.switchOff);
    }
});

async function sendPutRequest(endpoint) {
    const response = await fetch(endpoint, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ timer: false })
    });
    const data = await response.json();

    console.log(data.message);
    return data.message;
}

async function sendGetRequest(endpoint) {
    const response = await fetch(endpoint);
    const data = await response.json();

    console.log(data.message);
    return data.message;
}
