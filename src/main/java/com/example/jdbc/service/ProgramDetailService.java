package com.example.jdbc.service;

import com.example.jdbc.dto.ProgramDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProgramDetailService {


    private JdbcTemplate jdbcTemplate;

    public ProgramDetailService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Object programDetail(ProgramDetailDTO programDetailDTO) {
        System.out.println("fid " + programDetailDTO.getFIDProgram());
        System.out.println("fid " + programDetailDTO.getFIDProgram() == null);
        Map<String, Object> responseData = new LinkedHashMap<>();
        if(programDetailDTO.getFIDProgram() == null) {
            System.out.println("masuk");
            responseData.put("ResponseCode", "01");
            responseData.put("ResponseDescription", "Failed");
            responseData.put("ResponseException", "field request FIDProgam tidak ada");
            return responseData;
        } else {

            Map<String, Object> validateFIDProgram = this.validateFIDProgram(responseData, programDetailDTO.getFIDProgram());

            if (validateFIDProgram.get("ResponseCode").equals("01")){
                    return responseData;
            } else {

                String fidProgram = programDetailDTO.getFIDProgram();
                String sql = "select 1 from log_insert where FIDProgram = ?";
//                "SELECT EXISTS(SELECT FROM table WHERE email = ?)"
//                jdbcTemplate.query("SELECT * FROM log_insert WHERE FIDProgram = ?",  new Object[] { fidProgram }, Integer.class);
//                Integer query = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(0));
                  Boolean query = jdbcTemplate.query(sql, (ResultSet rs) -> {
                      if (rs.next()) {
                          return true;
                      }
                      return false;
                  }, new Object[] { fidProgram });
                System.out.println("hasil : " +query);

                if(!query) {
                    responseData.put("ResponseCode", "01");
                    responseData.put("ResponseDescription", "Failed");
                    responseData.put("ResponseException", "FIDProgram tidak valid, " +
                            "pastikan FIDProgram telah diselaraskan melalui modul ProgramInsert");

                    return responseData;
                }
                String sqlMitra = "\n" +
                        "SELECT\n" +
                        "b.kode_produk_mitra,\n" +
                        "b.kode_program_mitra,\n" +
                        "b.id_dd_bank,\n" +
                        "b.id_dc_produk,\n" +
                        "b.keterangan_produk,\n" +
                        "b.id_dc_peruntukan_kredit,\n" +
                        "b.flag_syariah,\n" +
                        "b.plafon_atas as PlafonMaksimal,\n" +
                        "b.plafon_bawah as PlafonMinimal,\n" +
                        "c.jangka_waktu_atas as JangkaWaktuMaksimal,\n" +
                        "c.jangka_waktu_bawah as JangkaWaktuMinimal,\n" +
                        "c.tarif as RatePremi,\n" +
                        "COALESCE(FIDSkemaPerhitunganBunga,1) AS FIDSkemaPerhitunganBunga,\n" +
                        "COALESCE(SkemaPerhitunganBunga,'Flat') AS SkemaPerhitunganBunga,\n" +
                        "b.id_dd_jenis_lain,\n" +
                        "COALESCE(jns.kode_mitra,0) as FIDKategoriJenisFasilitas,\n" +
                        "coalesce(jns.keterangan,'Tidak memerlukan pendefinisian jenis fasilitas') as KategoriJenisFasilitas,\n" +
                        "coalesce(c.cov_macet,0) as cov_macet,\n" +
                        "coalesce(c.cov_jiwa,0) as cov_jiwa,\n" +
                        "coalesce(c.cov_phk,0) as  cov_phk,\n" +
                        "coalesce(c.cov_kebakaran,0) as  cov_kebakaran,\n" +
                        "coalesce(b.plafon_cac,0) as  plafon_cac,\n" +
                        "c.ijp_minimal,\n" +
                        "a.nilai_pajak_feebase,\n" +
                        "a.nilai_feebase,\n" +
                        "b.total_eksposure,\n" +
                        "b.FIDJenisProduk,\n" +
                        "b.FIDProduk,\n" +
                        "b.NamaJenisProduk,\n" +
                        "b.NamaProduk,\n" +
                        "a.fid_acuan_covering,\n" +
                        "a.acuan_covering\n" +
                        "-- akhir tambahan BRISURF 2021-07\n" +
                        "FROM\n" +
                        "opdd_konfigurasi_ijp a with(nolock) \n" +
                        "JOIN dc_produk_mitra b with(nolock) ON a.id_opdd_konfigurasi_ijp = b.id_opdd_konfigurasi_ijp\n" +
                        "JOIN opdd_master_tarif c with(nolock) ON a.id_opdd_konfigurasi_ijp = c.id_opdd_konfigurasi_ijp\n" +
                        "AND COALESCE(c.id_dc_peruntukan_kredit,0) IN (\n" +
                        "0,\n" +
                        "b.id_dc_peruntukan_kredit\n" +
                        ")\n" +
                        "INNER JOIN dc_jns_pengajuan_mitra d ON a.id_opdd_konfigurasi_ijp = d.id_opdd_konfigurasi_ijp AND b.kode_program_mitra = d.FIDProgram\n" +
                        "LEFT JOIN dd_jenis_lain jns on jns.id_dd_jenis_lain = b.id_dd_jenis_lain\n" +
                        "WHERE\n" +
                        "a.id_dd_bank = 15\n" +
                        "AND GETDATE() BETWEEN tgl_awal_pks_sp3 AND tgl_akhir_pks_sp3\n" +
                        "AND kode_program_mitra = ?";
                Boolean query1 = jdbcTemplate.query(sqlMitra, (ResultSet rs) -> {
                    if (rs.next()) {
                        return true;
                    }
                    return false;
                }, fidProgram);
                System.out.println("query nya : " +query1);
                if(!query1) {
                    responseData.put("ResponseCode", "01");
                    responseData.put("ResponseDescription", "Failed");
                    responseData.put("ResponseException",
                            "FIDProgram belum dikonfigurasi oleh Jamkrindo (dimungkinkan karena ProgramInsert baru), harap disampaikan kepada tim TI Jamkrindo atau via divisi.ti@jamkrindo.co.id");

                    return responseData;
                }


            }

        }

        return responseData;
    }

    private Map<String, Object> validateFIDProgram(Map<String, Object> responseData, String fidProgram) {
        responseData.put("ResponseCode", "00");
        responseData.put("ResponseDescription", "Success");
        responseData.put("ResponseException", "");

        String pattern = "^[0-9]+";
        System.out.println("pattern " +fidProgram.matches(pattern));
        if(!fidProgram.matches(pattern)){
            responseData.put("ResponseCode", "01");
            responseData.put("ResponseDescription", "Failed");
            responseData.put("ResponseException", "field request FIDProgram ONly number");
            return responseData;
        }
        return responseData;
    }
}
