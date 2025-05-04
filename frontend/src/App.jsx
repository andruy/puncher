import { createBrowserRouter, RouterProvider } from "react-router-dom"
import "./App.css"
import { PrimeReactProvider } from 'primereact/api'
import "primereact/resources/themes/lara-dark-cyan/theme.css"
import "primereact/resources/primereact.min.css"
import 'primeicons/primeicons.css'
import "primeflex/primeflex.css"
import Layout from "./components/Layout"
import Home from "./components/Home"
import Features from "./components/Features"
import Programs from "./components/Programs"
import Logs from "./components/Logs"

const router = createBrowserRouter([
	{
		path: '/',
		element: <Layout />,
		children: [
			{
				index: true,
				element: <Home />,
			},
			{
				path: 'features',
				element: <Features />,
			},
			{
				path: 'programs',
				element: <Programs />,
			},
			{
				path: 'logs',
				element: <Logs />,
			},
		],
	},
])

function App() {
	return (
		<PrimeReactProvider value={{ ripple: true }}>
			<RouterProvider router={router} />
		</PrimeReactProvider>
	)
}

export default App
