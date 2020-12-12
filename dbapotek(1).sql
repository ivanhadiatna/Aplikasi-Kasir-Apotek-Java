-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 07, 2019 at 01:11 PM
-- Server version: 10.1.32-MariaDB
-- PHP Version: 5.6.36

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dbapotek`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_obat`
--

CREATE TABLE `tb_obat` (
  `kode_obat` varchar(20) NOT NULL,
  `nama_obat` varchar(100) NOT NULL,
  `kategori_obat` varchar(15) NOT NULL,
  `jenis_obat` varchar(20) NOT NULL,
  `merek_obat` varchar(100) NOT NULL,
  `harga_beli` decimal(10,0) NOT NULL,
  `harga_jual` decimal(10,0) NOT NULL,
  `jumlah_obat` int(3) NOT NULL,
  `tanggal_masuk` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expired` date NOT NULL,
  `kode_supplier` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_obat`
--

INSERT INTO `tb_obat` (`kode_obat`, `nama_obat`, `kategori_obat`, `jenis_obat`, `merek_obat`, `harga_beli`, `harga_jual`, `jumlah_obat`, `tanggal_masuk`, `expired`, `kode_supplier`) VALUES
('OBAT0001', 'Amoxcilin 100gr Generik', 'Obat Dalam', 'Kablet', 'Amoxcilin', '8000', '10000', 42, '2018-12-31 10:02:37', '2019-12-18', 'SUP001'),
('OBAT0002', 'Farsien 50 gr', 'Obat Dalam', 'Kapsul', 'Farsien', '6000', '8000', 38, '2018-12-31 10:04:01', '2019-10-23', 'SUP002'),
('OBAT0003', 'bioplacenton 15g', 'Obat Luar', 'Salep', 'bioplacenton', '25000', '27000', 38, '2019-01-02 11:07:55', '2022-01-02', 'SUP001'),
('OBAT0004', 'Diatabs 1 Pak', 'Obat Dalam', 'Kablet', 'Diatabs', '3500', '3700', 92, '2019-01-02 11:21:50', '2022-01-25', 'SUP002'),
('OBAT0005', 'Polysilane 100ml', 'Obat Dalam', 'Botol', 'Polysilane', '25000', '27000', 28, '2019-01-02 11:24:01', '2019-01-29', 'SUP002'),
('OBAT0006', 'Insto 7,5 ml', 'Obat Luar', 'Botol', 'Insto', '10000', '14000', 50, '2019-01-02 11:27:12', '2019-03-12', 'SUP001'),
('OBAT0007', 'Rohto 7 ml', 'Obat Luar', 'Botol', 'Rohto', '10000', '13000', 44, '2019-01-02 11:29:09', '2021-07-14', 'SUP001'),
('OBAT0008', 'Bisolvon 60ml', 'Obat Dalam', 'Botol', 'Bisolvon', '25000', '28000', 45, '2019-01-02 11:32:20', '2023-01-03', 'SUP002'),
('OBAT0009', 'Daktarin 10g', 'Obat Luar', 'Salep', 'Daktarin', '20000', '23000', 41, '2019-01-02 11:34:42', '2021-03-08', 'SUP001'),
('OBAT0010', 'Vital 10ml', 'Obat Luar', 'Botol', 'Vital', '10000', '13000', 43, '2019-01-07 10:38:23', '2022-01-05', 'SUP001'),
('OBAT0011', 'Otolin 10ml', 'Obat Luar', 'Botol', 'Otolin', '40000', '45000', 30, '2019-01-07 10:42:07', '2021-03-09', 'SUP001'),
('OBAT0012', 'Salonpas 30g', 'Obat Luar', 'Salep', 'SalonPas', '18000', '20000', 20, '2019-01-07 10:49:41', '2020-06-23', 'SUP001'),
('OBAT0013', 'Viotin DS 30kaplet', 'Obat Dalam', 'Kablet', 'Viotin DS', '130000', '150000', 13, '2019-01-07 10:51:25', '2021-01-01', 'SUP002'),
('OBAT0014', 'Neo Rheumacyl Cream Merah 30g', 'Obat Luar', 'Salep', 'Neo Rheumacyl', '11000', '13000', 10, '2019-01-07 10:56:34', '2019-01-15', 'SUP001');

-- --------------------------------------------------------

--
-- Table structure for table `tb_penjualan`
--

CREATE TABLE `tb_penjualan` (
  `id_jual` int(5) NOT NULL,
  `kode_transaksi` varchar(15) NOT NULL,
  `kode_obat` varchar(10) NOT NULL,
  `nama_obat` varchar(100) NOT NULL,
  `merek_obat` varchar(100) NOT NULL,
  `harga_jual` int(11) NOT NULL,
  `jumlah_jual` int(11) NOT NULL,
  `total_harga` int(11) NOT NULL,
  `tanggal_transaksi` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_penjualan`
--

INSERT INTO `tb_penjualan` (`id_jual`, `kode_transaksi`, `kode_obat`, `nama_obat`, `merek_obat`, `harga_jual`, `jumlah_jual`, `total_harga`, `tanggal_transaksi`) VALUES
(2, 'TRANS0001', 'OBAT0001', 'Amoxcilin 100gr', 'Amoxcilin', 10000, 1, 10000, '2019-01-07 10:20:54'),
(3, 'TRANS0001', 'OBAT0005', 'Polysilane 100ml', 'Polysilane', 27000, 1, 27000, '2019-01-07 10:22:02'),
(8, 'TRANS0002', 'OBAT0004', 'Diatabs 1 Pak', 'Diatabs', 3700, 1, 3700, '2019-01-07 11:19:21'),
(9, 'TRANS0003', 'OBAT0003', 'bioplacenton 15g', 'bioplacenton', 27000, 1, 27000, '2019-01-07 11:24:26');

-- --------------------------------------------------------

--
-- Table structure for table `tb_supplier`
--

CREATE TABLE `tb_supplier` (
  `kode_supplier` varchar(10) NOT NULL,
  `nama_supplier` varchar(50) NOT NULL,
  `jenis_kelamin` varchar(9) NOT NULL,
  `alamat` longtext NOT NULL,
  `no_hp` varchar(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_supplier`
--

INSERT INTO `tb_supplier` (`kode_supplier`, `nama_supplier`, `jenis_kelamin`, `alamat`, `no_hp`) VALUES
('SUP001', 'Raga', 'Laki-Laki', 'Lenteng Agung', '087'),
('SUP002', 'Risa', 'Perempuan', 'Jakarta Timur', '081356704532');

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE `tb_user` (
  `id_user` varchar(10) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `jenis_kelamin` varchar(15) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `akses` varchar(15) NOT NULL,
  `alamat` text NOT NULL,
  `email` varchar(50) NOT NULL,
  `no_hp` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_user`
--

INSERT INTO `tb_user` (`id_user`, `nama`, `jenis_kelamin`, `username`, `password`, `akses`, `alamat`, `email`, `no_hp`) VALUES
('ID27068850', 'HADIATNA', 'Laki-Laki', 'Hadi', '12345', 'Admin', 'KELAPA DUA', 'HADIATNA@MAIL.COM', '0877'),
('ID76504124', 'M.Fido', 'Laki-Laki', 'Fido', '123', 'Kasir', 'Kelapa Dua', 'FIDO@GMAIL.COM', '0819');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_obat`
--
ALTER TABLE `tb_obat`
  ADD PRIMARY KEY (`kode_obat`),
  ADD UNIQUE KEY `kode_obat` (`kode_obat`),
  ADD KEY `kode_supplier` (`kode_supplier`);

--
-- Indexes for table `tb_penjualan`
--
ALTER TABLE `tb_penjualan`
  ADD PRIMARY KEY (`id_jual`),
  ADD KEY `kode_obat` (`kode_obat`);

--
-- Indexes for table `tb_supplier`
--
ALTER TABLE `tb_supplier`
  ADD PRIMARY KEY (`kode_supplier`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_penjualan`
--
ALTER TABLE `tb_penjualan`
  MODIFY `id_jual` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_obat`
--
ALTER TABLE `tb_obat`
  ADD CONSTRAINT `tb_obat_ibfk_1` FOREIGN KEY (`kode_supplier`) REFERENCES `tb_supplier` (`kode_supplier`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_penjualan`
--
ALTER TABLE `tb_penjualan`
  ADD CONSTRAINT `tb_penjualan_ibfk_1` FOREIGN KEY (`kode_obat`) REFERENCES `tb_obat` (`kode_obat`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
