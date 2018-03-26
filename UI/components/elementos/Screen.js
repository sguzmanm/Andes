import React from 'react';
import  LinearGradient from 'react-native-linear-gradient';

const Screen = ({ children, style }) => <LinearGradient style={[{flex: 1}, style]} colors={['#E5CDF8', '#FFFFEF']}>{children}</LinearGradient>;

export default Screen;
