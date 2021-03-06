///////////////////////////////////////////////////////////////////// Package //
package jptw.sudoku;


///////////////////////////////////////////////////////////////////// Imports //

import org.apache.commons.lang3.SerializationUtils;

import javax.sql.rowset.serial.SerialBlob;
import javax.xml.bind.DataBindingException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;


/////////////////////////////////////////////////// Class: JdbcSudokuBoardDao //
public
class JdbcSudokuBoardDao
        implements AutoCloseable,
        Dao<SudokuBoard> {

    //=========================================================== Behaviour ==//
    //------------------------------------------------------ Constructors --==//
    JdbcSudokuBoardDao(final String sudokuBoardId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

        this.sudokuBoardId = sudokuBoardId;

        connection = connectToDerbyDatabase();
        if (!checkIfDatabaseHasAlreadyBeenCreated(connectToDerbyDatabase())) {
            createTableForSudokuBoards(connection);
            // Add checking for table existence
        }


    }


    //------------------------ Interface implementation: Dao<SudokuBoard> --==//
    @Override
    public SudokuBoard read() throws DBException, FileException {

        try {
            PreparedStatement sqlSelectSudokuBoardStatement
                    = connection.prepareStatement("select object from"
                    + " sudoku_boards where"
                    + " id = ?");
            sqlSelectSudokuBoardStatement.setString(1, sudokuBoardId);

            ResultSet resultSet
                    = sqlSelectSudokuBoardStatement.executeQuery();
            resultSet.next();


            Blob blob = resultSet.getBlob(1);
            InputStream byte_stream = blob.getBinaryStream();
            byte[] byte_array = new byte[4096];
            int bytes_read = byte_stream.read(byte_array);
            byte[] bytes_better = Arrays.copyOfRange(byte_array, 0, bytes_read);
            Object deSerializedObject =
                    SerializationUtils.deserialize(bytes_better);


            resultSet.close();
            sqlSelectSudokuBoardStatement.close();

            connection.close();

            return (SudokuBoard) deSerializedObject;

        } catch (SQLException sqlException) {
            throw new DBException(sqlException);
        } catch (Exception e) {
            throw new FileException(e);
        }

    }

    private Connection connectToDerbyDatabase()
            throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        Connection connection;
        try {
            connection = DriverManager.getConnection(jdbcDerbyConnectionPrompt);
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return connection;
    }

    private boolean checkIfDatabaseHasAlreadyBeenCreated(final Connection connection)
            throws SQLException {

        return connection.getMetaData()
                .getTables(null,
                        null,
                        "SUDOKU_BOARDS",
                        null)
                .next();
    }

    private void createTableForSudokuBoards(final Connection connection)
            throws SQLException {

        connection.createStatement()
                .execute("create table sudoku_boards"
                        + "("
                        + "    id varchar(30) not null"
                        + "        constraint primary_key_sudoku_boards_id"
                        + "            primary key,"
                        + ""
                        + "    name varchar(30),"
                        + ""
                        + "    object blob not null"
                        + ""
                        + ")");
    }

    private void writeObjectToDatabase(final Connection connection,
                                       final Object obj)
            throws SQLException {

        PreparedStatement deleteStatement = connection.prepareStatement(
                "delete from SUDOKU_BOARDS where id = '" + sudokuBoardId + "'"
        );
        deleteStatement.executeUpdate();

        // TODO: Write SQL statements as final Strings;
        PreparedStatement preparedStatement
                = connection.prepareStatement("insert into"
                + " sudoku_boards (id,"
                + "              name, "
                + "              object)"
                + " values (?, ?, ?)");
        preparedStatement.setString(1, sudokuBoardId);
        preparedStatement.setString(2, obj.getClass().getName());

        Blob blob = new SerialBlob(SerializationUtils
                .serialize((SudokuBoard) obj)
                .clone());
        preparedStatement.setBlob(3, blob);
        preparedStatement.executeUpdate();
    }

    @Override
    public void write(final SudokuBoard sudokuBoard) throws DBException {

        try {

            writeObjectToDatabase(connection,
                    sudokuBoard);

        } catch (SQLException sqlException) {
            throw new DBException(sqlException);
        }
    }

    @Override
    public void close() throws SQLException {

        try {
            connection.close();

        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    //================================================================ Data ==//
    private final
    Connection connection;

    private static final
    String databaseName = "sudokuGameDatabase";

    private static final
    String jdbcDerbyConnectionPrompt = "jdbc:derby:"
            + databaseName
            + ";create=true";

    private final
    String sudokuBoardId;


}


////////////////////////////////////////////////////////////////////////////////

