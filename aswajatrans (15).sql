-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 03, 2023 at 05:50 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aswajatrans`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `TampilBeberapaMobil` ()   BEGIN
    SELECT mobil.Nopol, mobil.MerkMobil,mobil.TypeMobil, mobil.harga FROM mobil WHERE STATUS = 'mobil belum disewa' ORDER BY mobil.harga ASC;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `TampilMobil` ()   BEGIN
    SELECT * FROM mobil ORDER BY harga ASC;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `TampilPelanggan` ()   BEGIN
    SELECT * FROM pelanggan ORDER BY pelanggan.Nama_Pelanggan ASC;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `detail_sewa`
--

CREATE TABLE `detail_sewa` (
  `id_Sewa` char(10) NOT NULL,
  `Nopol` char(10) DEFAULT NULL,
  `Tgl_Kembali` date NOT NULL,
  `Lama_Pinjam` int(11) NOT NULL,
  `tanggal_pengembalian` date DEFAULT NULL,
  `subtotal` int(11) NOT NULL,
  `Denda` int(11) NOT NULL,
  `Keterangan` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `detail_sewa`
--

INSERT INTO `detail_sewa` (`id_Sewa`, `Nopol`, `Tgl_Kembali`, `Lama_Pinjam`, `tanggal_pengembalian`, `subtotal`, `Denda`, `Keterangan`) VALUES
('TR0001', NULL, '2023-06-17', 2, '2023-06-16', 600000, 0, ' '),
('TR0002', 'N2123WE', '2023-06-13', 2, NULL, 600000, 0, ' '),
('TR0003', 'N7652IW', '2023-06-13', 2, NULL, 600000, 0, ' '),
('TR0004', NULL, '2023-06-17', 2, '2023-06-16', 600000, 0, ' '),
('TR0005', 'N34220RX', '2023-06-14', 2, NULL, 800000, 0, ' '),
('TR0006', 'N7652IO', '2023-06-14', 2, '2023-06-16', 600000, 0, ' '),
('TR0007', 'N7652IO', '2023-06-14', 2, '2023-06-16', 600000, 0, ' '),
('TR0008', NULL, '2023-06-17', 2, '2023-06-16', 600000, 0, ' '),
('TR0009', 'N7652IO', '2023-06-26', 3, NULL, 900000, 0, ' '),
('TR0010', 'N7652IW', '2023-11-24', 1, NULL, 300000, 0, ' '),
('TR0010', 'N8763WE', '2023-11-25', 2, NULL, 600000, 0, ' ');

--
-- Triggers `detail_sewa`
--
DELIMITER $$
CREATE TRIGGER `UpdateStatusMobil1` BEFORE INSERT ON `detail_sewa` FOR EACH ROW BEGIN
    IF NEW.tanggal_pengembalian IS NULL OR NEW.tanggal_pengembalian = '' THEN
        UPDATE mobil SET status = 'mobil sedang disewa' WHERE Nopol = NEW.Nopol;
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `UpdateStatusMobil2` AFTER UPDATE ON `detail_sewa` FOR EACH ROW BEGIN
    IF NEW.tanggal_pengembalian IS NOT NULL THEN
        UPDATE mobil SET status = 'mobil belum disewa' WHERE Nopol = NEW.Nopol;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `mobil`
--

CREATE TABLE `mobil` (
  `Nopol` char(10) NOT NULL,
  `MerkMobil` varchar(100) DEFAULT NULL,
  `TypeMobil` varchar(100) DEFAULT NULL,
  `harga` char(10) DEFAULT NULL,
  `Status` enum('Mobil Belum Disewa','Mobil Sedang Disewa','','') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mobil`
--

INSERT INTO `mobil` (`Nopol`, `MerkMobil`, `TypeMobil`, `harga`, `Status`) VALUES
('N2123WE', 'Avanza', 'Minibus', '300000', 'Mobil Belum Disewa'),
('N2123WI', 'Agya', 'Minibus', '300000', 'Mobil Belum Disewa'),
('N34220RX', 'Innova', 'Minibus', '400000', 'Mobil Belum Disewa'),
('N7652IO', 'Yaris', 'Minibus', '300000', 'Mobil Sedang Disewa'),
('N7652IW', 'Raize', 'Minibus', '300000', 'Mobil Sedang Disewa'),
('N8763WE', 'Xenia', 'Minibus', '300000', 'Mobil Sedang Disewa');

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `NIK` char(20) NOT NULL,
  `Nama_Pelanggan` varchar(100) DEFAULT NULL,
  `No_Telp` char(13) DEFAULT NULL,
  `Alamat` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`NIK`, `Nama_Pelanggan`, `No_Telp`, `Alamat`) VALUES
('3245643213243565', 'Roni Setiawan', '0862123146377', 'Jln Wonoasih'),
('341262876567283', 'Wira Atmaja', '0874567823452', 'Jln Mastrip gg kelapa kopyor Probolinggo'),
('341298502042420', 'Budi ', '0874567823452', 'Jln Mastrip gg kelapa muda Probolinggo'),
('341298502042421', 'Budi Atmaja', '0874567823452', 'Jln Mastrip gg kelapa muda Probolinggo'),
('341298502042423', 'Ratna Sulistiawati', '0874657892343', 'Jln Patiunus gg 07 Kota Probolinggo'),
('3574030308010003', 'Sony Kurniawan', '0857061343219', 'Jln Ikan Paus  Probolinggo'),
('357403203023002', 'Ratna Wijaya', '0874234542345', 'Jln Wiroborang no 14 Probolinggo'),
('357456789987657', 'Adigung', '0857061567822', 'Jln Kedungaem Probolinggo'),
('4023020004032002', 'Ranny Walidy', '0832345342546', 'Jln Panglima Sudirman'),
('402302000403212', 'Ranny W', '0832345342546', 'Jln Panglima Sudirman'),
('4053245234500204', 'Rachmad', '0832328429174', 'jln Wonoasih'),
('412343245043002', 'Rani Wahyuni', '0897665456723', 'Jln Triwung lor ');

-- --------------------------------------------------------

--
-- Table structure for table `pengeluaran`
--

CREATE TABLE `pengeluaran` (
  `id_pengeluaran` char(10) NOT NULL,
  `Tgl_Pengeluaran` date NOT NULL,
  `Keterangan` varchar(100) NOT NULL,
  `Nominal` int(10) NOT NULL,
  `Nopol` char(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengeluaran`
--

INSERT INTO `pengeluaran` (`id_pengeluaran`, `Tgl_Pengeluaran`, `Keterangan`, `Nominal`, `Nopol`) VALUES
('PNG0002', '2023-06-09', 'Service', 400000, NULL),
('PNG0003', '2023-06-03', 'service', 300000, NULL),
('PNG0004', '2023-06-15', 'Service', 100000, 'N7652IO'),
('PNG0005', '2023-06-16', 'Service', 100000, 'N7652IW');

-- --------------------------------------------------------

--
-- Table structure for table `sewa`
--

CREATE TABLE `sewa` (
  `id_Sewa` char(10) NOT NULL,
  `Tgl_sewa` date NOT NULL,
  `bayar` int(11) NOT NULL,
  `Sisa yang harus dibayar` int(11) NOT NULL,
  `StatusBayar` varchar(100) NOT NULL,
  `Total_Harga` int(11) NOT NULL,
  `Total_Sewa` int(11) NOT NULL,
  `NIK` char(20) NOT NULL,
  `id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sewa`
--

INSERT INTO `sewa` (`id_Sewa`, `Tgl_sewa`, `bayar`, `Sisa yang harus dibayar`, `StatusBayar`, `Total_Harga`, `Total_Sewa`, `NIK`, `id`) VALUES
('TR0001', '2023-06-09', 200000, 0, 'lunas', 600000, 1, '412343245043002', 14),
('TR0002', '2023-06-11', 400000, 200000, 'kurang', 600000, 1, '357456789987657', 23),
('TR0003', '2023-06-11', 100000, 0, 'lunas', 600000, 1, '341298502042423', 23),
('TR0004', '2023-06-11', 300000, 300000, 'kurang', 600000, 1, '4023020004032002', 23),
('TR0005', '2023-06-12', 300000, 500000, 'kurang', 800000, 1, '341298502042423', 23),
('TR0006', '2023-06-12', 300000, 0, 'lunas', 600000, 1, '412343245043002', 23),
('TR0007', '2023-06-15', 400000, 200000, 'kurang', 600000, 1, '341298502042423', 27),
('TR0008', '2023-06-15', 200000, 0, 'lunas', 600000, 2, '402302000403212', 33),
('TR0009', '2023-06-23', 10000000, 0, 'lunas', 900000, 1, '412343245043002', 23),
('TR0010', '2023-11-23', 400000, 500000, 'kurang', 900000, 2, '4053245234500204', 14);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `Username` varchar(100) NOT NULL,
  `Nama` varchar(100) NOT NULL,
  `TempatTanggalLahir` varchar(100) NOT NULL,
  `TanggalLahir` date NOT NULL,
  `NoTelpon` char(13) NOT NULL,
  `Hint` varchar(100) NOT NULL,
  `JawabanHint` varchar(100) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Foto` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `Username`, `Nama`, `TempatTanggalLahir`, `TanggalLahir`, `NoTelpon`, `Hint`, `JawabanHint`, `Password`, `Foto`) VALUES
(13, 'suyuthi', 'Suyuthi', 'Kota Probolinggo', '1998-05-20', '0847632782', 'Siapa nama ayahmu ??', 'agung', '123', NULL),
(14, 'totok', 'Totok Kurniawan', 'Probolinggo', '2001-08-03', '085706179485', 'Siapa nama ayahmu ??', 'Soleh ', 'totok', 'C:\\Users\\ndraa\\Documents\\content-december-thumbnail-1034x580-4-yqpex16wnb.jpg'),
(23, 'admin2', 'jodhy', 'Probolinggo', '2001-06-16', '0872312345632', 'Siapa nama ayahmu ??', 'Haris', '123', 'D:\\Lain-lain\\Foto\\Mt Buthak\\IMG_20230617_085829.jpg'),
(27, 'Team2', 'Team2', 'Jember', '2023-06-01', '0854267643879', 'Apa warna favoritmu ??', 'kuning', 'Team2', NULL),
(33, 'totok1', 'Totok Kurniawan', 'Jember', '2001-08-03', '0865234565434', 'Apa makanan kesukaanmu ??', 'Nasgor', 'totok1', NULL),
(39, 'Sony', 'Sony', '3', '2023-06-10', '08624345', 'Siapa nama ayahmu ??', 'sony', 'Sony', 'D:\\Lain-lain\\Foto\\Bekel\\IMG-20220623-WA0007.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `detail_sewa`
--
ALTER TABLE `detail_sewa`
  ADD KEY `id_Sewa` (`id_Sewa`),
  ADD KEY `Nopol` (`Nopol`);

--
-- Indexes for table `mobil`
--
ALTER TABLE `mobil`
  ADD PRIMARY KEY (`Nopol`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`NIK`);

--
-- Indexes for table `pengeluaran`
--
ALTER TABLE `pengeluaran`
  ADD PRIMARY KEY (`id_pengeluaran`),
  ADD KEY `Nopol` (`Nopol`);

--
-- Indexes for table `sewa`
--
ALTER TABLE `sewa`
  ADD PRIMARY KEY (`id_Sewa`),
  ADD KEY `id_Pelanggan` (`NIK`),
  ADD KEY `id_user` (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detail_sewa`
--
ALTER TABLE `detail_sewa`
  ADD CONSTRAINT `detail_sewa` FOREIGN KEY (`id_Sewa`) REFERENCES `sewa` (`id_Sewa`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `detail_sewa_ibfk_1` FOREIGN KEY (`Nopol`) REFERENCES `mobil` (`Nopol`) ON DELETE NO ACTION;

--
-- Constraints for table `pengeluaran`
--
ALTER TABLE `pengeluaran`
  ADD CONSTRAINT `pengeluaran_ibfk_1` FOREIGN KEY (`Nopol`) REFERENCES `mobil` (`Nopol`) ON DELETE NO ACTION;

--
-- Constraints for table `sewa`
--
ALTER TABLE `sewa`
  ADD CONSTRAINT `pel` FOREIGN KEY (`NIK`) REFERENCES `pelanggan` (`NIK`) ON DELETE CASCADE,
  ADD CONSTRAINT `user` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
