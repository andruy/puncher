import { useState } from "react"
import { createBrowserRouter, RouterProvider } from "react-router-dom"
import { Outlet } from "react-router-dom"
import { PrimeReactProvider } from 'primereact/api'
import "primereact/resources/themes/lara-dark-cyan/theme.css"
import "primereact/resources/primereact.min.css"
import 'primeicons/primeicons.css'
import "primeflex/primeflex.css"
import './App.css'
import MenuButton from "./components/MenuButton"
import Loading from "./components/Loading"
import Home from "./components/Home"
import Features from "./components/Features"
import NotFound from "./components/NotFound"

function Layout({ visible }) {
	return (
		<>
			<MenuButton />
			<Loading state={visible} />
			<Outlet />
		</>
	)
}

const router = (visible, setVisible) => createBrowserRouter([
	{
		path: "/",
		element: <Layout visible={visible} />,
		children: [
			{ index: true, element: <Home setVisible={setVisible} /> },
			{ path: "features", element: <Features setVisible={setVisible} /> },
			{ path: "*", element: <NotFound /> },
		]
	}
])

function App() {
	const [visible, setVisible] = useState(false)

	return (
		<PrimeReactProvider value={{ ripple: true }}>
			<RouterProvider router={router(visible, setVisible)} />
		</PrimeReactProvider>
	)
}

export default App
