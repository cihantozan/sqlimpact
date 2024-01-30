import java.io.FileNotFoundException;
import java.sql.SQLException;

import net.sf.jsqlparser.JSQLParserException;
import sqlimpact.util.DBUtil;
import sqlimpact.util.SqlTreeUtil;

public class Deneme {
public static void main(String[] args) throws FileNotFoundException, JSQLParserException, ClassNotFoundException, SQLException {
		
		String sql="INSERT\n" + 
				"	INTO\n" + 
				"	CDMKART.STGKART_TF_ASIL_MUST_KRK_AKTIF(\n" + 
				"		TARIH,\n" + 
				"		ASIL_KART_MUSTERI_ID,\n" + 
				"		SUBE_KODU,\n" + 
				"		MIY_SEGMENT_KODU,\n" + 
				"		KRK_AKTIF_MTD_FLAG,\n" + 
				"		KRK_AKTIF_SON_30_FLAG,\n" + 
				"		KRK_AKTIF_SON_90_FLAG,\n" + 
				"		KRK_AKTIF_BIREYSEL_MTD_FLAG,\n" + 
				"		KRK_AKTIF_BIREYSEL_SON_30_FLAG,\n" + 
				"		KRK_AKTIF_BIREYSEL_SON_90_FLAG,\n" + 
				"		KRK_AKTIF_TICARI_MTD_FLAG,\n" + 
				"		KRK_AKTIF_TICARI_SON_30_FLAG,\n" + 
				"		KRK_AKTIF_TICARI_SON_90_FLAG,\n" + 
				"		KRK_AKTIF_TIC_TEDARIK_MTD_FLAG,\n" + 
				"		KRK_AKTIF_TIC_TEDARK_S_30_FLAG,\n" + 
				"		KRK_AKTIF_TIC_TEDARK_S_90_FLAG,\n" + 
				"		KRK_AKTIFLES_YENI_MUST_FLAG,\n" + 
				"		KRK_AKTIFLES_MEVCUT_MUST_FLAG,\n" + 
				"		KRK_INAKTIFLES_MUST_FLAG,\n" + 
				"		KRK_KAYBEDILEN_MUST_FLAG,\n" + 
				"		KRK_BRY_AKTFLS_YENI_MUST_FLAG,\n" + 
				"		KRK_BRY_AKTFLS_MEV_MUST_FLAG,\n" + 
				"		KRK_BRY_INAKTIFLES_MUST_FLAG,\n" + 
				"		KRK_BRY_KAYBEDILEN_MUST_FLAG,\n" + 
				"		KRK_TIC_AKTFLS_YENI_MUST_FLAG,\n" + 
				"		KRK_TIC_AKTFLS_MEV_MUST_FLAG,\n" + 
				"		KRK_TIC_INAKTIFLES_MUST_FLAG,\n" + 
				"		KRK_TIC_KAYBEDILEN_MUST_FLAG,\n" + 
				"		INSERT_DATE,\n" + 
				"		KRK_SON_90_MAX_ISLEM_TARIHI,\n" + 
				"		KRK_BRY_SON_90_MAX_ISLEM_TARIH,\n" + 
				"		KRK_TIC_SON_90_MAX_ISLEM_TARIH,\n" + 
				"		KRK_TIC_TEDRK_S_90_MAX_ISL_TRH\n" + 
				"	)\n" + 
				"SELECT\n" + 
				"	CAST(\n" + 
				"		PM_VAYSSXPHCEBTRZV4MX7W5YTG7ZE.TARIH AS DATE\n" + 
				"	),\n" + 
				"	PM_VPHTGQKVOAQ4HLMNSGJVE775XTU.MUSTERI_ID,\n" + 
				"	PM_VPHTGQKVOAQ4HLMNSGJVE775XTU.SUBE_KODU,\n" + 
				"	PM_VPHTGQKVOAQ4HLMNSGJVE775XTU.MIY_SEGMENT_KODU,\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_MTD_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_MTD_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_SON_30_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_SON_30_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_SON_90_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_SON_90_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_MTD_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_MTD_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_SON_30_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_SON_30_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_SON_90_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_SON_90_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_MTD_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_MTD_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_SON_30_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_SON_30_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_SON_90_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_SON_90_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TIC_TEDARIK_MTD_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TIC_TEDARIK_MTD_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TIC_TEDARK_S_30_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TIC_TEDARK_S_30_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	(\n" + 
				"	CASE\n" + 
				"			WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TIC_TEDARK_S_90_FLAG IS NULL THEN 0\n" + 
				"			ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TIC_TEDARK_S_90_FLAG\n" + 
				"		END\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.ASIL_KART_MUSTERI_ID IS NULL\n" + 
				"					AND (\n" + 
				"						PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_SON_30_FLAG = 0\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_SON_30_FLAG = 0\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"					AND PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_SON_30_FLAG IS NULL\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						(\n" + 
				"						CASE\n" + 
				"								WHEN PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_ACIK_BRY_KART_ADET IS NULL THEN 0\n" + 
				"								ELSE PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_ACIK_BRY_KART_ADET\n" + 
				"							END\n" + 
				"						) = 0\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						(\n" + 
				"							PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_SON_30_FLAG = 1\n" + 
				"						)\n" + 
				"						AND (\n" + 
				"							(\n" + 
				"							CASE\n" + 
				"									WHEN PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_BIREYSEL_SON_30_FLAG IS NULL THEN 0\n" + 
				"									ELSE PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_BIREYSEL_SON_30_FLAG\n" + 
				"								END\n" + 
				"							) = 0\n" + 
				"						)\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						(\n" + 
				"						CASE\n" + 
				"								WHEN PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_ACIK_BRY_KART_ADET IS NULL THEN 0\n" + 
				"								ELSE PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_ACIK_BRY_KART_ADET\n" + 
				"							END\n" + 
				"						) > 0\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						(\n" + 
				"							(\n" + 
				"							CASE\n" + 
				"									WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_ACIK_BRY_KART_ADET IS NULL THEN 0\n" + 
				"									ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_ACIK_BRY_KART_ADET\n" + 
				"								END\n" + 
				"							) > 0\n" + 
				"						)\n" + 
				"						AND (\n" + 
				"							(\n" + 
				"							CASE\n" + 
				"									WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_SON_30_FLAG IS NULL THEN 0\n" + 
				"									ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_BIREYSEL_SON_30_FLAG\n" + 
				"								END\n" + 
				"							) = 0\n" + 
				"						)\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_BIREYSEL_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						(\n" + 
				"						CASE\n" + 
				"								WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_ACIK_BRY_KART_ADET IS NULL THEN 0\n" + 
				"								ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_ACIK_BRY_KART_ADET\n" + 
				"							END\n" + 
				"						) = 0\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_BIREYSEL_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						(\n" + 
				"						CASE\n" + 
				"								WHEN PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_ACIK_TIC_KART_ADET IS NULL THEN 0\n" + 
				"								ELSE PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_ACIK_TIC_KART_ADET\n" + 
				"							END\n" + 
				"						) = 0\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						(\n" + 
				"							PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_SON_30_FLAG = 1\n" + 
				"						)\n" + 
				"						AND (\n" + 
				"							(\n" + 
				"							CASE\n" + 
				"									WHEN PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_TICARI_SON_30_FLAG IS NULL THEN 0\n" + 
				"									ELSE PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_TICARI_SON_30_FLAG\n" + 
				"								END\n" + 
				"							) = 0\n" + 
				"						)\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						(\n" + 
				"						CASE\n" + 
				"								WHEN PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_ACIK_TIC_KART_ADET IS NULL THEN 0\n" + 
				"								ELSE PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_ACIK_TIC_KART_ADET\n" + 
				"							END\n" + 
				"						) > 0\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						(\n" + 
				"							(\n" + 
				"							CASE\n" + 
				"									WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_ACIK_TIC_KART_ADET IS NULL THEN 0\n" + 
				"									ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_ACIK_TIC_KART_ADET\n" + 
				"								END\n" + 
				"							) > 0\n" + 
				"						)\n" + 
				"						AND (\n" + 
				"							(\n" + 
				"							CASE\n" + 
				"									WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_SON_30_FLAG IS NULL THEN 0\n" + 
				"									ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_AKTIF_TICARI_SON_30_FLAG\n" + 
				"								END\n" + 
				"							) = 0\n" + 
				"						)\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_TICARI_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		(\n" + 
				"		CASE\n" + 
				"				WHEN (\n" + 
				"					(\n" + 
				"						(\n" + 
				"						CASE\n" + 
				"								WHEN PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_ACIK_TIC_KART_ADET IS NULL THEN 0\n" + 
				"								ELSE PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_ACIK_TIC_KART_ADET\n" + 
				"							END\n" + 
				"						) = 0\n" + 
				"					)\n" + 
				"					AND (\n" + 
				"						PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.KRK_AKTIF_TICARI_SON_30_FLAG = 1\n" + 
				"					)\n" + 
				"				) THEN 1\n" + 
				"				ELSE 0\n" + 
				"			END\n" + 
				"		) AS FLOAT\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		CURRENT_DATE AS DATE\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_SON_90_MAX_ISLEM_TARIHI AS DATE\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_BRY_SON_90_MAX_ISLEM_TARIH AS DATE\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_TIC_SON_90_MAX_ISLEM_TARIH AS DATE\n" + 
				"	),\n" + 
				"	CAST(\n" + 
				"		PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.KRK_TIC_TEDRK_S_90_MAX_ISL_TRH AS DATE\n" + 
				"	)\n" + 
				"FROM\n" + 
				"	(\n" + 
				"		(\n" + 
				"			(\n" + 
				"				(\n" + 
				"					PM_VAYSSXPHCEBTRZV4MX7W5YTG7ZE\n" + 
				"				INNER JOIN PM_VPHTGQKVOAQ4HLMNSGJVE775XTU ON\n" + 
				"					(\n" + 
				"						PM_VPHTGQKVOAQ4HLMNSGJVE775XTU.TARIH = PM_VAYSSXPHCEBTRZV4MX7W5YTG7ZE.TARIH\n" + 
				"					)\n" + 
				"				)\n" + 
				"			INNER JOIN CDMKART.STGKART_TF_ASIL_MUST_KKAKT_KTL PM_AB7IDFIGKXD5KUC3KCGREWUJ2EY ON\n" + 
				"				(\n" + 
				"					PM_AB7IDFIGKXD5KUC3KCGREWUJ2EY.ASIL_KART_MUSTERI_ID = PM_VPHTGQKVOAQ4HLMNSGJVE775XTU.MUSTERI_ID\n" + 
				"				)\n" + 
				"			)\n" + 
				"		LEFT OUTER JOIN CDMKART.STGKART_TF_ASIL_MUST_KKAKT_LST PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI ON\n" + 
				"			(\n" + 
				"				(\n" + 
				"					PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.TARIH = PM_VAYSSXPHCEBTRZV4MX7W5YTG7ZE.TARIH\n" + 
				"				)\n" + 
				"				AND (\n" + 
				"					PM_A7M2PRRRQPCOYSCR3IV44TLW5ZI.ASIL_KART_MUSTERI_ID = PM_VPHTGQKVOAQ4HLMNSGJVE775XTU.MUSTERI_ID\n" + 
				"				)\n" + 
				"			)\n" + 
				"			AND (\n" + 
				"				1 <> 0\n" + 
				"			)\n" + 
				"		)\n" + 
				"	LEFT OUTER JOIN CDMKART.STGKART_TF_ASIL_MUST_KKAKT_LST PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM ON\n" + 
				"		(\n" + 
				"			(\n" + 
				"				PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.TARIH = PM_VAYSSXPHCEBTRZV4MX7W5YTG7ZE.ONCEKI_AYSONU\n" + 
				"			)\n" + 
				"			AND (\n" + 
				"				PM_AJ6O3OVUCGLJBF72UNL4PDCLFAM.ASIL_KART_MUSTERI_ID = PM_VPHTGQKVOAQ4HLMNSGJVE775XTU.MUSTERI_ID\n" + 
				"			)\n" + 
				"		)\n" + 
				"		AND (\n" + 
				"			1 <> 0\n" + 
				"		)\n" + 
				"	)\n" + 
				"WHERE\n" + 
				"	(\n" + 
				"		1 <> 0\n" + 
				"	)\n" + 
				"	AND (\n" + 
				"		1 <> 0\n" + 
				"	)\n" + 
				"	AND (\n" + 
				"		1 <> 0\n" + 
				"	)";
		
		DBUtil dbutil=new DBUtil();
		dbutil.getMetadataFromFile("c:\\gecici\\dbmetadata.csv");
		System.out.println("metadata file read ok.");
		//dbutil.getMetadataFromDB();
		
		dbutil.getViewListFromFile("c:\\gecici\\views.csv");
		System.out.println("views file read ok.");
		
		SqlTreeUtil sqlTreeUtil=new SqlTreeUtil();
		sqlTreeUtil.parse(sql, "CDMKART", dbutil);
				
		
		System.out.println("finished");

	}
}
