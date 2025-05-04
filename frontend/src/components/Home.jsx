import { useState, useRef, useEffect } from 'react'
import { InputSwitch } from "primereact/inputswitch"
import { Button } from "primereact/button"
import { Toast } from 'primereact/toast'
import Loading from './Loading'

export default function Home() {
	const rootPath = ""
	const [checked, setChecked] = useState(false)
	const [loadingVisible, setLoadingVisible] = useState(false)
	const [isDisabled, setIsDisabled] = useState(true)
	const toast = useRef(null)

	const endpoints = {
		checkStatus: rootPath + '/check',
		switchState: rootPath + '/switchState',
		switchOn: rootPath + '/switchOn',
		switchOff: rootPath + '/switchOff'
	}

	useEffect(() => {
		sendGetRequest(endpoints.switchState)
		healthCheck()
	}, [])

	async function healthCheck() {
		const response = await fetch(rootPath + '/healthCheck')

		if (response.ok) {
			const data = await response.json()
			setIsDisabled(!data.message)
		}
	}

	async function sendPutRequest(endpoint) {
		const response = await fetch(endpoint, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			}
		})

		if (response.ok) {
			const data = await response.json()
			console.log(data.message)

			if (data.message === 'Something went wrong') {
				toast.current.show({ severity: 'error', summary: 'Error', detail: data.message })
			} else {
				toast.current.show({ severity: 'success', summary: 'Success', detail: data.message })
			}
		} else {
			console.error(response)

			toast.current.show({ severity: 'error', summary: 'Error', detail: 'Something went wrong' })
		}
	}

	async function sendGetRequest(endpoint) {
		if (endpoint === endpoints.checkStatus) setLoadingVisible(true)

		const response = await fetch(endpoint)

		if (response.ok) {
			const data = await response.json()
			console.log(data.message)
			if (endpoint === endpoints.checkStatus) setLoadingVisible(false)

			if (data.message === 'Something went wrong') {
				toast.current.show({ severity: 'error', summary: 'Error', detail: data.message })
			} else {
				toast.current.show({ severity: 'info', summary: 'State', detail: data.message})
				if (endpoint === endpoints.switchState) {
					setChecked(data.state)
				}
			}
		} else {
			console.error(response)
			if (endpoint === endpoints.checkStatus) setLoadingVisible(false)

			toast.current.show({ severity: 'error', summary: 'Error', detail: 'Something went wrong' })
		}
	}

	return (
		<>
			<Loading state={loadingVisible} />
			<div className="centeredColumn">
				<Toast ref={toast} position="bottom-center" />
				<InputSwitch checked={checked} onChange={e => { sendPutRequest(e.value ? endpoints.switchOn : endpoints.switchOff) && setChecked(e.value) }} disabled={isDisabled} />
				<Button rounded label='Check Status' icon='pi pi-clock' severity="warning" onClick={() => sendGetRequest(endpoints.checkStatus)} />
			</div>
		</>
	)
}
