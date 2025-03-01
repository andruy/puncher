"use client"

import { useState, useRef } from 'react'
import { InputSwitch } from "primereact/inputswitch"
import { Button } from "primereact/button"
import { Toast } from 'primereact/toast'
import Loading from './Loading'
import styles from './page.module.css'

export default function Home() {
	const [checked, setChecked] = useState(false)
	const [loadingVisible, setLoadingVisible] = useState(false)
	const toast = useRef(null)

	const endpoints = {
		checkStatus: '/check',
		switchState: '/switchState',
		switchOn: '/switchOn',
		switchOff: '/switchOff'
	}

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

	async function sendGetRequest(endpoint) {
		setLoadingVisible(true)

		const response = await fetch(endpoint)

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
				<InputSwitch checked={checked} onChange={e => setChecked(e.value)} />
				<Button rounded label='Check Status' icon='pi pi-clock' severity="warning" onClick={() => sendGetRequest(endpoints.checkStatus)} />
			</div>
		</>
	)
}
