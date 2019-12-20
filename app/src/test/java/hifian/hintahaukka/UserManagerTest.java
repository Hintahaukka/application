package hifian.hintahaukka;

import android.content.Context;

import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import hifian.hintahaukka.Service.UserManager;

public class UserManagerTest {

    private UserManager userManager;
    private final SPMockBuilder spMockBuilder = new SPMockBuilder();

    @Mock
    Context mockContext = Mockito.mock(Context.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockContext.getString(R.string.key_points_total)).thenReturn("key_points_total");
        when(mockContext.getString(R.string.key_points_unused)).thenReturn("key_points_unused");
        when(mockContext.getString(R.string.rank_1)).thenReturn("Linnunmuna");
        when(mockContext.getString(R.string.rank_8)).thenReturn("Hintahaukka");
        Context c = new SPMockBuilder().createContext();
        this.userManager = new UserManager(c, mockContext);
    }

    @Test
    public void pointsInTheBeginningAreZero() {
        assertTrue(userManager.getPointsTotal()==0);
        assertTrue(userManager.getPointsUnused()==0);
    }

    @Test
    public void pointsCanBeUpdated() {
        userManager.updatePoints(20, 10);
        assertTrue(userManager.getPointsTotal()==20);
        assertTrue(userManager.getPointsUnused()==10);
    }

    @Test
    public void rankIsDefinedCorrectly() {
        userManager.updatePoints(1, 0);
        assertEquals("Linnunmuna", userManager.getRank());
        userManager.updatePoints(2000, 100);
        assertEquals("Hintahaukka", userManager.getRank());
    }


}
