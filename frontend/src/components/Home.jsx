import { useState } from "react"
import { InputSwitch } from "primereact/inputswitch"
import { Button } from "primereact/button"

function Home({ setVisible }) {
	const [checked, setChecked] = useState(false)

    return (
        <div className="centeredColumn">
            <InputSwitch checked={checked} onChange={e => setChecked(e.value)} />
            <Button rounded label='Check Status' icon='pi pi-clock' severity="warning" onClick={() => setVisible(true)} />
        </div>
    )
}

export default Home
