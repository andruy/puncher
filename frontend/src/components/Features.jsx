import { useState } from 'react'
import { Button } from "primereact/button"
import { Dialog } from "primereact/dialog"

export default function Features() {
    const [clockInVisible, setClockInVisible] = useState(false)
    const [clockOutVisible, setClockOutVisible] = useState(false)

    const clockInFooter = (
        <div>
            <Button label="No" icon="pi pi-times" onClick={() => setClockInVisible(false)} className="p-button-text" />
            <Button label="Yes" icon="pi pi-check" onClick={() => setClockInVisible(false)} autoFocus />
        </div>
    )

    const clockOutFooter = (
        <div>
            <Button label="No" icon="pi pi-times" onClick={() => setClockOutVisible(false)} className="p-button-text" />
            <Button label="Yes" icon="pi pi-check" onClick={() => setClockOutVisible(false)} autoFocus />
        </div>
    )

    return (
        <div className="centeredColumn">
            <Button rounded label='Clock In' icon='pi pi-key' severity="success" onClick={() => setClockInVisible(true)} />
            <Dialog header="🟢" visible={clockInVisible} style={{ width: '50vw' }} onHide={() => {if (!clockInVisible) return; setClockInVisible(false); }} footer={clockInFooter}>
                <p className="m-0">
                    Are you sure you want to clock in?
                </p>
            </Dialog>
            <Button rounded label='Clock Out' icon='pi pi-power-off' severity="danger" onClick={() => setClockOutVisible(true)} />
            <Dialog header="🔴" visible={clockOutVisible} style={{ width: '50vw' }} onHide={() => {if (!clockOutVisible) return; setClockOutVisible(false); }} footer={clockOutFooter}>
                <p className="m-0">
                    Are you sure you want to clock out?
                </p>
            </Dialog>
        </div>
    )
}
