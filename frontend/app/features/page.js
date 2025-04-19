"use client"

import { useState, useRef } from 'react'
import { Button } from "primereact/button"
import { Dialog } from "primereact/dialog"
import { Toast } from 'primereact/toast'
import Loading from '../Loading'
import styles from '../page.module.css'

export default function Features() {
	const rootPath = process.env.NEXT_PUBLIC_API_URL
    const [clockInVisible, setClockInVisible] = useState(false)
    const [clockOutVisible, setClockOutVisible] = useState(false)
    const [loadingVisible, setLoadingVisible] = useState(false)
    const toast = useRef(null)

    const endpoints = {
        clockIn: rootPath + '/clockIn',
        clockOut: rootPath + '/clockOut'
    }

    const clockInFooter = (
        <div>
            <Button label="No" icon="pi pi-times" onClick={() => setClockInVisible(false)} className="p-button-text" />
            <Button label="Yes" icon="pi pi-check" onClick={() => sendPutRequest(endpoints.clockIn) && setClockInVisible(false)} autoFocus />
        </div>
    )

    const clockOutFooter = (
        <div>
            <Button label="No" icon="pi pi-times" onClick={() => setClockOutVisible(false)} className="p-button-text" />
            <Button label="Yes" icon="pi pi-check" onClick={() => sendPutRequest(endpoints.clockOut) && setClockOutVisible(false)} autoFocus />
        </div>
    )

    async function sendPutRequest(endpoint) {
        setLoadingVisible(true)

        const response = await fetch(endpoint, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ timer: false })
        })

		if (response.ok) {
			const data = await response.json()
			console.log(data.message)
			setLoadingVisible(false)

            if (data.message === 'Something went wrong') {
                toast.current.show({ severity: 'error', summary: 'Error', detail: data.message })
            } else {
                toast.current.show({ severity: 'success', summary: 'Success', detail: data.message })
            }
		} else {
            console.error(response)
			setLoadingVisible(false)

			toast.current.show({ severity: 'error', summary: 'Error', detail: 'Something went wrong' })
        }
    }

    return (
        <>
            <Loading state={loadingVisible} />
            <div className={styles.centeredColumn}>
				<Toast ref={toast} position="bottom-center" />
                <Button rounded label='Clock In' icon='pi pi-key' severity="success" onClick={() => setClockInVisible(true)} />
                <Dialog header="🟢" visible={clockInVisible} style={{ width: '50vw' }} onHide={() => { if (!clockInVisible) return; setClockInVisible(false) }} footer={clockInFooter}>
                    <p className="m-0">
                        Are you sure you want to clock in?
                    </p>
                </Dialog>
                <Button rounded label='Clock Out' icon='pi pi-power-off' severity="danger" onClick={() => setClockOutVisible(true)} />
                <Dialog header="🔴" visible={clockOutVisible} style={{ width: '50vw' }} onHide={() => { if (!clockOutVisible) return; setClockOutVisible(false) }} footer={clockOutFooter}>
                    <p className="m-0">
                        Are you sure you want to clock out?
                    </p>
                </Dialog>
            </div>
        </>
    )
}
