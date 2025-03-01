"use client"

import { useState } from 'react'
import { InputSwitch } from "primereact/inputswitch"
import { Button } from "primereact/button"
import Loading from './Loading'
import styles from './page.module.css'

export default function Home() {
	const [checked, setChecked] = useState(false)
	const [visible, setVisible] = useState(false)

	return (
		<>
			<Loading props={visible} />
			<div className={styles.centeredColumn}>
				<InputSwitch checked={checked} onChange={e => setChecked(e.value)} />
				<Button rounded label='Check Status' icon='pi pi-clock' severity="warning" onClick={() => setVisible(true)} />
				{visible && <p>Status is now visible!</p>}
			</div>
		</>
	)
}
