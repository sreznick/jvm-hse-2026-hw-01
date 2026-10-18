import com.jsoizo.kotlincsv.csvReader
import com.jsoizo.kotlincsv.reader.readFromFile
import structures.RawProductInfo
import java.io.File


class CsvReader {
    fun read(path: String) : List<RawProductInfo> {
        val file = File(path)

        return csvReader().readFromFile(file) {
            rows -> rows.drop(1).mapIndexed { ind, row ->
                RawProductInfo(
                    rowNum = ind + 1,
                    id = row[0],
                    name = row[1],
                    type = row[2],
                    price = row[3],
                    weight = row[4],
                    vehicle = row[5],
                    extra = row[6]
                )
            }.toList()
        }
    }
}