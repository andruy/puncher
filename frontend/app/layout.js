import { Geist, Geist_Mono } from "next/font/google"
import { PrimeReactProvider } from 'primereact/api'
import MenuButton from "./MenuButton"
import "primereact/resources/themes/lara-dark-cyan/theme.css"
import "primereact/resources/primereact.min.css"
import 'primeicons/primeicons.css'
import "primeflex/primeflex.css"
import "./globals.css"

const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
})

const geistMono = Geist_Mono({
    variable: "--font-geist-mono",
    subsets: ["latin"],
})

export const metadata = {
    title: "Puncher 2.0",
    description: "Think of it as TimeClock on steroids.",
}

export default function RootLayout({ children }) {
    return (
        <html lang="en">
            <body>
                <MenuButton />
                <PrimeReactProvider value={{ ripple: true }}>
                    {children}
                </PrimeReactProvider>
            </body>
        </html>
    )
}
