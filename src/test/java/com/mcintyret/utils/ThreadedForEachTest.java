package com.mcintyret.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Uninterruptibles;
import com.mcintyret.utils.concurrent.ProcessorFactory;
import com.mcintyret.utils.concurrent.ThreadedForEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.synchronizedSet;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.AssertJUnit.assertEquals;

/**
 * User: mcintyret2
 * Date: 14/05/2013
 */

@Test
public class ThreadedForEachTest {

    @Mock
    private ProcessorFactory<Integer> mockProcessorFactory;
    @Captor
    private ArgumentCaptor<Iterator<Integer>> iteratorCaptor;

    private ThreadedForEach<Integer> threadedForEach;

    private Set<Integer> executed;

    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);
        when(mockProcessorFactory.getIterable()).thenReturn(Lists.newArrayList(1, 2, 3, 4, 5));
        when(mockProcessorFactory.newRunnable(anyInt())).thenAnswer(new Answer<Runnable>() {
            @Override
            public Runnable answer(final InvocationOnMock invocation) throws Throwable {
                return new Runnable() {
                    @Override
                    public void run() {
                        executed.add((Integer) invocation.getArguments()[0]);
                    }
                };
            }
        });

        threadedForEach = new ThreadedForEach<>(mockProcessorFactory);
        executed = synchronizedSet(Sets.<Integer>newHashSet());
    }

    public void shouldRun() {
        // given
        when(mockProcessorFactory.shouldExecute(anyInt())).thenReturn(true);

        // when
        threadedForEach.execute();

        // then
        verify(mockProcessorFactory).beforeExecution();
        verify(mockProcessorFactory).afterExecution();
        verify(mockProcessorFactory, never()).onCancellation(any(Iterator.class));

        assertEquals(Sets.newHashSet(1, 2, 3, 4, 5), executed);
    }

    public void shouldSkipWhenShouldExecuteReturnsFalse() {
        // given
        when(mockProcessorFactory.shouldExecute(1)).thenReturn(true);
        when(mockProcessorFactory.shouldExecute(3)).thenReturn(true);
        when(mockProcessorFactory.shouldExecute(5)).thenReturn(true);

        // when
        threadedForEach.execute();

        // then
        verify(mockProcessorFactory).beforeExecution();
        verify(mockProcessorFactory).afterExecution();
        verify(mockProcessorFactory, never()).onCancellation(any(Iterator.class));

        assertEquals(Sets.newHashSet(1, 3, 5), executed);
    }

    public void shouldStop() {
        // given
        when(mockProcessorFactory.shouldExecute(anyInt())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
                return true;
            }
        });

        when(mockProcessorFactory.newRunnable(anyInt())).thenAnswer(new Answer<Runnable>() {
            @Override
            public Runnable answer(final InvocationOnMock invocation) throws Throwable {
                return new Runnable() {
                    @Override
                    public void run() {
                        int i = (Integer) invocation.getArguments()[0];
                        if (i == 3) {
                            threadedForEach.stop();
                        }
                        executed.add((Integer) invocation.getArguments()[0]);
                    }
                };
            }
        });

        // when
        threadedForEach.execute();

        //then
        verify(mockProcessorFactory).beforeExecution();
        verify(mockProcessorFactory).afterExecution();
        verify(mockProcessorFactory).onCancellation(iteratorCaptor.capture());

        assertEquals(Lists.newArrayList(5), Lists.newArrayList(iteratorCaptor.getValue()));

        assertEquals(Sets.newHashSet(1, 2, 3, 4), executed);
    }

}
